package com.placute.ocrbackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class LicensePlate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plateNumber;

    private LocalDateTime detectedAt;

    private String imagePath;

    // Constructori
    public LicensePlate() {}

    public LicensePlate(String plateNumber, String imagePath) {
        this.plateNumber = plateNumber;
        this.imagePath = imagePath;
        this.detectedAt = LocalDateTime.now();
    }

    // Getteri și setteri
    public Long getId() {
        return id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public LocalDateTime getDetectedAt() {
        return detectedAt;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
