package com.placute.ocrbackend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToOne
    private LicensePlate licensePlate;

    public Insurance() {}

    public Insurance(String provider, LocalDate startDate, LocalDate endDate, LicensePlate licensePlate) {
        this.provider = provider;
        this.startDate = startDate;
        this.endDate = endDate;
        this.licensePlate = licensePlate;
    }

    public Long getId() {
        return id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LicensePlate getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(LicensePlate licensePlate) {
        this.licensePlate = licensePlate;
    }
}