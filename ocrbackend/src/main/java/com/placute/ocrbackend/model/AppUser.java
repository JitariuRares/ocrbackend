package com.placute.ocrbackend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role; // enum în loc de String

    @OneToMany(mappedBy = "user")
    private List<LicensePlate> licensePlates;

    // Getteri și setteri

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<LicensePlate> getLicensePlates() {
        return licensePlates;
    }

    public void setLicensePlates(List<LicensePlate> licensePlates) {
        this.licensePlates = licensePlates;
    }
}
