package com.placute.ocrbackend.repository;

import com.placute.ocrbackend.model.LicensePlate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicensePlateRepository extends JpaRepository<LicensePlate, Long> {
    // opțional: metode personalizate de căutare
    LicensePlate findByPlateNumber(String plateNumber);
}
