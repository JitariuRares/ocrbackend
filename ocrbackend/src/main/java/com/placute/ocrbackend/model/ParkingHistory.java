package com.placute.ocrbackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ParkingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    @ManyToOne
    private LicensePlate licensePlate;

    public ParkingHistory() {}

    public ParkingHistory(LocalDateTime checkIn, LocalDateTime checkOut, LicensePlate licensePlate) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.licensePlate = licensePlate;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }

    public LicensePlate getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(LicensePlate licensePlate) {
        this.licensePlate = licensePlate;
    }
}