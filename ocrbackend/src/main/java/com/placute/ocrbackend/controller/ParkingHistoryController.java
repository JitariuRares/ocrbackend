package com.placute.ocrbackend.controller;

import com.placute.ocrbackend.model.ParkingHistory;
import com.placute.ocrbackend.repository.ParkingHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingHistoryController {

    @Autowired
    private ParkingHistoryRepository parkingHistoryRepository;

    @PreAuthorize("hasRole('PARKING')")
    @PostMapping
    public ParkingHistory addParkingRecord(@RequestBody ParkingHistory record) {
        return parkingHistoryRepository.save(record);
    }

    @PreAuthorize("hasRole('PARKING')")
    @GetMapping("/{plateNumber}")
    public List<ParkingHistory> getParkingByPlate(@PathVariable String plateNumber) {
        return parkingHistoryRepository.findByLicensePlate_PlateNumber(plateNumber);
    }
}
