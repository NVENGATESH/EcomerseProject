//package com.ecommerce.project.security.service;
//import com.ecommerce.project.model.AppRole;
//import com.ecommerce.project.model.Role;
//import com.ecommerce.project.model.User;
//import com.ecommerce.project.repositories.RoleRepository;
//import com.ecommerce.project.repositories.UserRepository;
//import com.ecommerce.project.security.jwt.JwtUtils;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.Value;
//import org.springframework.http.ResponseCookie;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
////
//@Component
//public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
//
//    private final JwtUtils jwtUtils;
//    private final UserRepository userRepository;
//    private final String redirectUri;
//
//    public OAuth2LoginSuccessHandler(JwtUtils jwtUtils, UserRepository userRepository) {
//        this.jwtUtils = jwtUtils;
//        this.userRepository = userRepository;
//    }
//
//    public OAuth2LoginSuccessHandler(@Value(staticConstructor = "${app.oauth2.redirect-uri}") String redirectUri) {
//        this.redirectUri = redirectUri;
//    }
////
////    @Override
////    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
////        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
////        String email = oAuth2User.getAttribute("email");
////
////        // Optional: create user in DB if doesn't exist
////        User user = userRepository.findByEmail(email)
////                .orElseGet(() -> {
////                    User newUser = new User();
////                    newUser.setEmail(email);
////                    newUser.setUserName(oAuth2User.getAttribute("name"));
////                    return userRepository.save(newUser);
////                });
////
////        // Generate JWT cookie using email
////        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(email);
////
////        // Add cookie to response
////        response.addHeader("Set-Cookie", jwtCookie.toString());
////
////        // Send response
////        response.setStatus(HttpServletResponse.SC_OK);
////    }
////}
//
////    @Override
////    public void onAuthenticationSuccess(HttpServletRequest request,
////                                        HttpServletResponse response,
////                                        Authentication authentication) throws IOException {
////        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
////        String email = oAuth2User.getAttribute("email");
////        String name = oAuth2User.getAttribute("name");
////
////        // Optional: create user in DB if doesn't exist
////        User user = userRepository.findByEmail(email)
////                .orElseGet(() -> {
////                    User newUser = new User();
////                    newUser.setEmail(email);
////                    newUser.setUserName(name);
////                    return userRepository.save(newUser);
////                });
////
////        // Generate JWT cookie
////        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(email);
////        response.addHeader("Set-Cookie", jwtCookie.toString());
////
////        // Return JSON response
////        response.setContentType("application/json");
////        response.setCharacterEncoding("UTF-8");
////
////        String json = String.format("{\"name\":\"%s\",\"email\":\"%s\"}", name, email);
////        response.getWriter().write(json);
////        response.getWriter().flush();
////    }
//
////    @Override
////    public void onAuthenticationSuccess(HttpServletRequest request,
////                                        HttpServletResponse response,
////                                        Authentication authentication) throws IOException {
////        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
////        String email = oAuth2User.getAttribute("email");
////        String name = oAuth2User.getAttribute("name");
////
////        // Optional: save user in DB
////        User user = userRepository.findByEmail(email)
////                .orElseGet(() -> {
////                    User newUser = new User();
////                    newUser.setEmail(email);
////                    newUser.setUserName(name);
////                    return userRepository.save(newUser);
////                });
////
////        // Generate JWT cookie
////        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(email);
////        response.addHeader("Set-Cookie", jwtCookie.toString());
////
////        // Return JSON response directly
////        response.setContentType("application/json");
////        response.setCharacterEncoding("UTF-8");
////        response.getWriter().write(
////                String.format("{\"name\":\"%s\", \"email\":\"%s\", \"picture\":\"%s\"}",
////                        name,
////                        email,
////                        oAuth2User.getAttribute("picture"))
////        );
////        response.getWriter().flush();
////    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        Authentication authentication) throws IOException {
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        String email = oAuth2User.getAttribute("email");
//        String name = oAuth2User.getAttribute("name");
//
//        // Optional: save user in DB if not exists
//        User user = userRepository.findByEmail(email)
//                .orElseGet(() -> {
//                    User newUser = new User();
//                    newUser.setEmail(email);
//                    newUser.setUserName(name);
//                    return userRepository.save(newUser);
//                });
//
//        // Generate JWT token
//        String jwt = jwtUtils.generateTokenFromEmail(email); // make sure JwtUtils has this method
//
//        // Optionally, set JWT in HttpOnly cookie
//        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(email); // your existing method
//        response.addHeader("Set-Cookie", jwtCookie.toString());
//
//        // Return JSON response with JWT and user info
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
////        response.getWriter().write(
////                String.format(
////                        "{\"name\":\"%s\", \"email\":\"%s\", \"picture\":\"%s\", \"token\":\"%s\"}",
////                        name,
////                        email,
////                        oAuth2User.getAttribute("picture"),
////                        jwt
////                )
////        );
////        response.getWriter().write("{\"status\":\"ok\"}");
//        response.getWriter().write(
//                String.format("{\"name\":\"%s\", \"email\":\"%s\", \"token\":\"%s\"}", name, email, jwt)
//        );
////        response.sendRedirect("http://localhost:5173/eco-store");
//        response.getWriter().flush();
//    }
//
//
//}

package com.ecommerce.project.security.service;

import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final String redirectUri;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public OAuth2LoginSuccessHandler(JwtUtils jwtUtils, UserRepository userRepository,
                                     @Value("${app.oauth2.redirect-uri}") String redirectUri) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.redirectUri = redirectUri;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Get Google's "email_verified" claim
        Boolean emailVerifiedAttr = oAuth2User.getAttribute("email_verified");
        boolean emailVerified = emailVerifiedAttr != null && emailVerifiedAttr;

        // Save user in DB if not exists
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUserName(name);
                    return userRepository.save(newUser);
                });

        // Generate JWT with proper email_verified flag
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(email, emailVerified, true);
        response.addHeader("Set-Cookie", jwtCookie.toString());

        // Redirect to frontend
        redirectStrategy.sendRedirect(request, response, redirectUri);
    }

}

