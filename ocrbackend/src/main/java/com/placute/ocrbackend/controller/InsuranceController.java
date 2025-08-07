package com.placute.ocrbackend.controller;

import com.placute.ocrbackend.model.Insurance;
import com.placute.ocrbackend.repository.InsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurance")
public class InsuranceController {

    @Autowired
    private InsuranceRepository insuranceRepository;

    @PreAuthorize("hasAnyRole('INSURANCE', 'POLICE')")
    @GetMapping("/{plateNumber}")
    public List<Insurance> getInsuranceByPlate(@PathVariable String plateNumber) {
        return insuranceRepository.findByLicensePlate_PlateNumber(plateNumber);
    }


    @PreAuthorize("hasRole('INSURANCE')")
    @PostMapping
    public ResponseEntity<Insurance> saveInsurance(@RequestBody Insurance insurance) {
        Insurance saved = insuranceRepository.save(insurance);
        return ResponseEntity.ok(saved);
    }

}
