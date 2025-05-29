package com.placute.ocrbackend.controller;

import com.placute.ocrbackend.model.LicensePlate;
import com.placute.ocrbackend.repository.LicensePlateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/license-plates")
public class LicensePlateController {

    @Autowired
    private LicensePlateRepository licensePlateRepository;

    @PreAuthorize("hasRole('POLICE')")
    @GetMapping
    public List<LicensePlate> getAllPlates() {
        return licensePlateRepository.findAll();
    }

    @PreAuthorize("hasRole('POLICE')")
    @GetMapping("/{plateNumber}")
    public List<LicensePlate> getByPlateNumber(@PathVariable String plateNumber) {
        return licensePlateRepository.findByPlateNumber(plateNumber);
    }


    @PreAuthorize("hasAnyRole('POLICE', 'PARKING')")
    @PostMapping
    public LicensePlate savePlate(@RequestBody LicensePlate plate) {
        return licensePlateRepository.save(plate);
    }
}
