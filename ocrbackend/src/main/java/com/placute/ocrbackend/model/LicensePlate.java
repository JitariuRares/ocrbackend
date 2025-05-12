package com.placute.ocrbackend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class LicensePlate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plateNumber;

    private LocalDateTime detectedAt;

    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "licensePlate", cascade = CascadeType.ALL)
    private List<ParkingHistory> parkingHistory;

    @OneToMany(mappedBy = "licensePlate", cascade = CascadeType.ALL)
    private List<Insurance> insurances;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ParkingHistory> getParkingHistory() {
        return parkingHistory;
    }

    public void setParkingHistory(List<ParkingHistory> parkingHistory) {
        this.parkingHistory = parkingHistory;
    }

    public List<Insurance> getInsurances() {
        return insurances;
    }

    public void setInsurances(List<Insurance> insurances) {
        this.insurances = insurances;
    }
}
