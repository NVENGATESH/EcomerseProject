
package com.ecommerce.project.controller;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.OtpEntity;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.OtpRepository;
import com.ecommerce.project.repositories.RoleRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.security.EmailService;
import com.ecommerce.project.security.EmailValidator;
import com.ecommerce.project.security.jwt.JwtUtils;
import com.ecommerce.project.security.request.LoginRequest;
import com.ecommerce.project.security.request.OtpRequest;
import com.ecommerce.project.security.request.OtpService;
import com.ecommerce.project.security.request.SignupRequest;
import com.ecommerce.project.security.response.MessageResponse;
import com.ecommerce.project.security.response.UserInfoResponse;
import com.ecommerce.project.security.service.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(
        origins = {
                "https://eco-store-git-main-nvengateshs-projects.vercel.app/",
                "https://eco-store-five.vercel.app",
                "http://localhost:5173"
        },
        allowCredentials = "true"
)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private OtpService otpService;
    // ---------------- LOGIN ----------------
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .collect(Collectors.toList());

            UserInfoResponse response = new UserInfoResponse(
                    userDetails.getId(),
                    userDetails.getUsername(),
                    roles,
                    jwtCookie.toString(),
                    userDetails.getEmail()
            );

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Bad credentials", "status", false));
        }
    }



    @GetMapping("/oauth2user")
    public ResponseEntity<?> oauth2User(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof DefaultOAuth2User oauth2User)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not a Google OAuth2 user");
        }

        String email = oauth2User.getAttribute("email");
        Boolean emailVerified = oauth2User.getAttribute("email_verified");

        if (email == null || !emailVerified) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Google account email is not verified. Please verify your email."));
        }

        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new User();
            user.setUserName(oauth2User.getAttribute("name"));
            user.setEmail(email);
            user.setEmailVerified(true);

            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role not found."));
            user.setRoles(Set.of(userRole));

            userRepository.save(user);
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", oauth2User.getAttribute("name"));
        userInfo.put("email", email);
        userInfo.put("emailVerified", true);
        userInfo.put("oauth2", true);
        userInfo.put("imageUrl", oauth2User.getAttribute("picture"));

        return ResponseEntity.ok(userInfo);
    }

    // ---------------- LOGOUT ----------------
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        request.getSession().invalidate();

        Cookie jwtCookie = new Cookie("globelTechJwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        Cookie sessionCookie = new Cookie("JSESSIONID", null);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(0);
        response.addCookie(sessionCookie);

        Map<String, Object> result = new HashMap<>();
        result.put("message", "Successfully logged out");
        result.put("oauth2LogoutUrl", "https://accounts.google.com/Logout");

        return ResponseEntity.ok(result);
    }





    @Transactional
    @PostMapping("/send-otp-signup")
    public ResponseEntity<?> sendOtpForSignup(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already registered! Please login."));
        }

        otpService.deleteByEmail(email); // <-- transactional delete

        String otp = String.valueOf((int)((Math.random() * 900000) + 100000));
        OtpEntity otpEntity = new OtpEntity(email, otp, Instant.now().plusSeconds(300));
        otpRepository.save(otpEntity);


        emailService.sendVerificationEmail(email, "Verify your email", "Your OTP is: " + otp);

        return ResponseEntity.ok(new MessageResponse("OTP sent to your email!"));
    }


    @PostMapping("/verify-otp-signup")
    public ResponseEntity<?> verifyOtpSignup(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        OtpEntity otpEntity = otpRepository.findTopByEmailOrderByExpiryDateDesc(email)
                .orElseThrow(() -> new RuntimeException("OTP not found!"));

        if (otpEntity.getExpiryDate().isBefore(Instant.now())) {
            otpRepository.deleteByEmail(email); // cleanup expired
            return ResponseEntity.badRequest().body(new MessageResponse("OTP expired!"));
        }

        if (!otpEntity.getOtp().equals(otp)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid OTP!"));
        }

        otpRepository.deleteByEmail(email); // delete after success
        return ResponseEntity.ok(new MessageResponse("OTP verified! You can now complete signup."));
    }


    // ---------------- COMPLETE SIGNUP ----------------
    @PostMapping("/signup")
    public ResponseEntity<?> completeSignup(@RequestBody SignupRequest request) {

        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is required!"));
        }

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email is required!"));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already registered!"));
        }

        if (!EmailValidator.isValidEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid email!"));
        }

        if (userRepository.existsByUserName(request.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username already exists!"));
        }

        // create user
        User user = new User();
        user.setUserName(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setEmailVerified(true);

        userRepository.save(user);

        // delete OTP record just in case
        otpRepository.deleteByEmail(request.getEmail());

        return ResponseEntity.ok(new MessageResponse("Signup completed successfully!"));
    }

//     ---------------- CURRENT USER INFO ----------------
    @GetMapping("/userinfoss")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        Object principal = auth.getPrincipal();
        Map<String, Object> userInfo = new HashMap<>();

        if (principal instanceof DefaultOAuth2User oauth2User) {
            userInfo.put("name", oauth2User.getAttribute("name"));
            userInfo.put("email", oauth2User.getAttribute("email"));
            userInfo.put("imageUrl", oauth2User.getAttribute("picture"));
            userInfo.put("oauth2", true);
            userInfo.put("emailVerified", oauth2User.getAttribute("email_verified"));
        } else if (principal instanceof UserDetailsImpl user) {
            userInfo.put("name", user.getUsername());
            userInfo.put("email", user.getEmail());
            userInfo.put("imageUrl", "https://w7.pngwing.com/pngs/178/595/png-transparent-user-profile-computer-icons-login-user-avatars-thumbnail.png");
            userInfo.put("oauth2", false);
            userInfo.put("emailVerified", user.isEmailVerified());
        }

        return ResponseEntity.ok(userInfo);
    }

}
