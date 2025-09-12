package com.ecommerce.project.security;

import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.security.EmailValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String email, String password) {

        if (!EmailValidator.isValidEmail(email)) {
            throw new RuntimeException("Invalid email address.");
        }

        // Block Gmail for manual signup
        if (email.toLowerCase().endsWith("@gmail.com")) {
            throw new RuntimeException("Please register using Google Sign-In for Gmail accounts.");
        }

        // MX record check
        String domain = email.substring(email.indexOf("@") + 1);
        if (!MxChecker.hasMxRecord(domain)) {
            throw new RuntimeException("Email domain does not exist.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already in use.");
        }

        if (userRepository.existsByUserName(username)) {
            throw new RuntimeException("Username is already taken.");
        }

        User user = new User();
        user.setUserName(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmailVerified(false);

        return userRepository.save(user);
    }

}
