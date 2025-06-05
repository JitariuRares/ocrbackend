// src/main/java/com/placute/ocrbackend/model/OcrHistory.java
package com.placute.ocrbackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entitate care stochează fiecare procesare OCR:
 * - licensePlate: referința către LicensePlate
 * - filename: numele fișierului încărcat
 * - processedAt: timestamp al procesării
 */
@Entity
@Table(name = "ocr_history")
public class OcrHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Legătură ManyToOne spre LicensePlate.
     * Coloana în baza de date va fi license_plate_id.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "license_plate_id", nullable = false)
    private LicensePlate licensePlate;

    // Păstrăm numele fișierului încărcat (opțional)
    @Column(name = "filename")
    private String filename;

    // Timestamp-ul la care s-a rulat OCR
    @Column(name = "processed_at", nullable = false)
    private LocalDateTime processedAt;

    // Constructor gol – necesar Hibernate
    public OcrHistory() { }

    /**
     * Constructor convenabil
     * @param licensePlate entitatea LicensePlate deja salvată
     * @param filename     numele fișierului încărcat
     * @param processedAt  LocalDateTime.now()
     */
    public OcrHistory(LicensePlate licensePlate, String filename, LocalDateTime processedAt) {
        this.licensePlate = licensePlate;
        this.filename = filename;
        this.processedAt = processedAt;
    }

    // Getteri și setteri:

    public Long getId() {
        return id;
    }

    public LicensePlate getLicensePlate() {
        return licensePlate;
    }
    public void setLicensePlate(LicensePlate licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
}
