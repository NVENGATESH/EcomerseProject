package com.ecommerce.project.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "otp_entity")
public class OtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email; // always required for signup/login OTP

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = true) // optional for signup flow
    private User user;

    @Column(nullable = false)
    private String otp;

    @Column(nullable = false)
    private Instant expiryDate;

    public OtpEntity() {}

    public OtpEntity(User user, String otp, Instant expiryDate) {
        this.user = user;
        this.email = user.getEmail();
        this.otp = otp;
        this.expiryDate = expiryDate;
    }

    public OtpEntity(String email, String otp, Instant expiryDate) {
        this.email = email;
        this.otp = otp;
        this.expiryDate = expiryDate;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
    public Instant getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Instant expiryDate) { this.expiryDate = expiryDate; }
}
