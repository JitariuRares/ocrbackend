package com.placute.ocrbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "license_plate")
public class LicensePlate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Numărul plăcuței
    @Column(name = "plate_number", nullable = false, unique = true)
    private String plateNumber;

    // Data și ora la care a fost detectată prima dată
    @Column(name = "detected_at", nullable = false)
    private LocalDateTime detectedAt;

    // Calea (sau numele) fișierului imagine
    @Column(name = "image_path")
    private String imagePath;

    // Marca mașinii (nou)
    @Column(name = "brand")
    private String brand;

    // Modelul mașinii (nou)
    @Column(name = "model")
    private String model;

    // Numele proprietarului (nou)
    @Column(name = "owner")
    private String owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private AppUser user;

    @OneToMany(mappedBy = "licensePlate", cascade = CascadeType.ALL)
    private List<ParkingHistory> parkingHistory;

    @OneToMany(mappedBy = "licensePlate", cascade = CascadeType.ALL)
    private List<Insurance> insurances;

    public LicensePlate() { }

    // Constructor relevant: setează plateNumber, imagePath și detectedAt
    public LicensePlate(String plateNumber, String imagePath) {
        this.plateNumber = plateNumber;
        this.imagePath = imagePath;
        this.detectedAt = LocalDateTime.now();
    }

    // Getteri și setteri pentru toate câmpurile

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
    public void setDetectedAt(LocalDateTime detectedAt) {
        this.detectedAt = detectedAt;
    }

    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public AppUser getUser() {
        return user;
    }
    public void setUser(AppUser user) {
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
