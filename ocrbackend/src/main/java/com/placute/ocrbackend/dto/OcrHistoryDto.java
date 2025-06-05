package com.placute.ocrbackend.dto;

import java.time.LocalDateTime;

public class OcrHistoryDto {

    private Long id;
    private String plateNumber;
    private String imagePath;      // calea/numele imaginii din LicensePlate
    private LocalDateTime processedAt;

    public OcrHistoryDto(Long id, String plateNumber, String imagePath, LocalDateTime processedAt) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.imagePath = imagePath;
        this.processedAt = processedAt;
    }

    // Getteri:
    public Long getId() {
        return id;
    }
    public String getPlateNumber() {
        return plateNumber;
    }
    public String getImagePath() {
        return imagePath;
    }
    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
}
