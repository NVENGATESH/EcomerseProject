package com.ecommerce.project.security.jwt;
import com.ecommerce.project.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${spring.app.jwtCookieName}")
    private String jwtCookie;

    // Get JWT from cookie
    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        return cookie != null ? cookie.getValue() : null;
    }

    // Generate cookie from UserDetailsImpl
    // Generate cookie from UserDetailsImpl (normal signup/login users → verified = true)
//    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
//        String jwt = generateTokenFromEmail(userPrincipal.getEmail(), true); // ✅ always verified for DB users
//        return ResponseCookie.from(jwtCookie, jwt)
//                .path("/")
//                .maxAge(jwtExpirationMs / 1000)
//                .httpOnly(true)
//                .sameSite("None")
//                .secure(true)
//                .build();
//    }

    // JwtUtils.java
//    public ResponseCookie generateJwtCookie(String email) {
//        String token = generateTokenFromEmail(email);
//        return ResponseCookie.from(jwtCookie, token)
//                .path("/")
//                .maxAge(jwtExpirationMs / 1000)
//                .httpOnly(true)
//                .sameSite("None")
//                .secure(true)
//
//                .build();
//    }



    public ResponseCookie generateJwtCookie(String email, boolean emailVerified, boolean isProd) {
        String token = generateTokenFromEmail(email, emailVerified);
        return ResponseCookie.from(jwtCookie, token)
                .path("/")
                .maxAge(jwtExpirationMs / 1000)
                .httpOnly(true)
                .sameSite(isProd ? "None" : "Lax")
                .secure(isProd) // ✅ false in localhost, true in production
                .build();
    }


    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        return generateJwtCookie(userPrincipal.getEmail(), true, false); // ✅ always true
    }






    // Generate JWT token from email
    public String generateTokenFromEmail(String email, boolean emailVerified) {
        return Jwts.builder()
                .setSubject(email)
                .claim("email_verified", emailVerified)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }
    public boolean isEmailVerified(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("email_verified", Boolean.class);
    }


    // Get email from JWT token
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Validate JWT token
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }

    // Clean cookie
    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookie, null)
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .build();
    }


    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    public Cookie generateJwtServletCookie(UserDetailsImpl userPrincipal) {
        String token =  generateTokenFromEmail(userPrincipal.getEmail(), true);
        Cookie cookie = new Cookie(jwtCookie, token);
        cookie.setPath("/");
        cookie.setMaxAge(jwtExpirationMs / 1000);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true in prod
        return cookie;
    }


}
