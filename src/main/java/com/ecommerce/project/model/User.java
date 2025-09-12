//package com.ecommerce.project.model;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;
//import lombok.*;
//
//import com.ecommerce.project.model.Role;
//import java.util.HashSet;
//import java.util.Set;
//@Entity
//@Table(name = "users")
//@Data
//@AllArgsConstructor
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "user_id")
//    private Long userId;
//
//    @NotBlank
//    @Size(max = 50)
//    @Column(name = "username", nullable = false, unique = true)
//    private String userName;
//
//    @NotBlank
//    @Size(max = 50)
//    @Email
//    @Column(name = "email", nullable = false, unique = true)
//    private String email;
//
//
//
//    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinTable(name = "user_role",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private Set<Role> roles = new HashSet<>();
//
//    public Long getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Long userId) {
//        this.userId = userId;
//    }
//
//    public @NotBlank @Size(max = 50) String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(@NotBlank @Size(max = 50) String userName) {
//        this.userName = userName;
//    }
//
//    public @NotBlank @Size(max = 50) @Email String getEmail() {
//        return email;
//    }
//
//    public void setEmail(@NotBlank @Size(max = 50) @Email String email) {
//        this.email = email;
//    }
//
//    public void setPassword(@Size(max = 120) String password) {
//        this.password = password;
//    }
//
//    @Size(max = 120)
//    @Column(name = "password", nullable = true)
//    private String password;
//
//    public String getPassword() {
//        return password;
//    }
//
//    public Set<Role> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<Role> roles) {
//        this.roles = roles;
//    }
//
//    public User(String username, String email, String password) {
//        this.userName = username;
//        this.email = email;
//        this.password = password;
//    }
//    // Public no-args constructor for Hibernate
//    public User() {
//    }
//}


package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data

public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "user_id")
//    private Long userId;
//
//    @NotBlank
//    @Size(max = 50)
//    @Column(name = "username", nullable = false, unique = true)
//    private String userName;
//
//    @NotBlank
//    @Size(max = 50)
//    @Email
//    @Column(name = "email", nullable = false, unique = true)
//    private String email;
//
//    @Size(max = 120)
//    @Column(name = "password", nullable = true)
//    private String password;
//
//    @Column(name = "email_verified")
//    private Boolean emailVerified = false;
//
//    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinTable(name = "user_role",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private Set<Role> roles = new HashSet<>();
//
//    public Long getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Long userId) {
//        this.userId = userId;
//    }
//
//    public @NotBlank @Size(max = 50) String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(@NotBlank @Size(max = 50) String userName) {
//        this.userName = userName;
//    }
//
//    public @NotBlank @Size(max = 50) @Email String getEmail() {
//        return email;
//    }
//
//    public void setEmail(@NotBlank @Size(max = 50) @Email String email) {
//        this.email = email;
//    }
//
//    public @Size(max = 120) String getPassword() {
//        return password;
//    }
//
//    public void setPassword(@Size(max = 120) String password) {
//        this.password = password;
//    }
//
//    public boolean isEmailVerified() {
//        return emailVerified;
//    }
//
//    public void setEmailVerified(boolean emailVerified) {
//        this.emailVerified = emailVerified;
//    }
//
//    public Set<Role> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<Role> roles) {
//        this.roles = roles;
//    }
//
//    public User(String username, String email, String password) {
//        this.userName = username;
//        this.email = email;
//        this.password = password;
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @NotBlank
    @Size(max = 50)
    @Column(name = "username", nullable = false, unique = true)
    private String userName;


    @NotBlank
    @Size(max = 50)
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Size(max = 120)
    @Column(nullable = true)
    private String password;

    @Column(columnDefinition = "boolean default false")
    private Boolean emailVerified;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String username, String email, String encode) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public User(Long userId, String userName, String email, String password, Boolean emailVerified, Set<Role> roles) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.emailVerified = emailVerified;
        this.roles = roles;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public @NotBlank @Size(max = 50) String getUserName() {
        return userName;
    }

    public void setUserName(@NotBlank @Size(max = 50) String userName) {
        this.userName = userName;
    }

    public @NotBlank @Size(max = 50) @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Size(max = 50) @Email String email) {
        this.email = email;
    }

    public @Size(max = 120) String getPassword() {
        return password;
    }

    public void setPassword(@Size(max = 120) String password) {
        this.password = password;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}


