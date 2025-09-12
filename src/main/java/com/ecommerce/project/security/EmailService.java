package com.ecommerce.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String subject, String body) {
        System.out.println("===== OTP EMAIL SIMULATION =====");
        System.out.println("To: " + toEmail);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        System.out.println("===============================");

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("vengatesh10122003@gmail.com"); // correct sender
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("OTP email sent to: " + toEmail);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
}

