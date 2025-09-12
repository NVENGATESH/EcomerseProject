//package com.ecommerce.project.model;
//// src/main/java/com/ecommerce/project/model/VerificationToken.java
//
//
//import jakarta.persistence.*;
//import java.time.Instant;
//
//@Entity
//@Table(name = "verification_tokens")
//public class VerificationToken {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, unique = true)
//    private String token;
//
//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(nullable = false, name = "user_id")
//    private User user;
//
//    @Column(nullable = false)
//    private Instant expiryDate;
//
//    public VerificationToken() {}
//
//    public VerificationToken(String token, User user, Instant expiryDate) {
//        this.token = token;
//        this.user = user;
//        this.expiryDate = expiryDate;
//    }
//
//    // getters & setters
//    public Long getId() { return id; }
//    public String getToken() { return token; }
//    public void setToken(String token) { this.token = token; }
//    public User getUser() { return user; }
//    public void setUser(User user) { this.user = user; }
//    public Instant getExpiryDate() { return expiryDate; }
//    public void setExpiryDate(Instant expiryDate) { this.expiryDate = expiryDate; }
//}
