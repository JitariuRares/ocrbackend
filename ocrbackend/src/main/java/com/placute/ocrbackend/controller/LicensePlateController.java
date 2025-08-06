package com.placute.ocrbackend.controller;

import com.placute.ocrbackend.model.LicensePlate;
import com.placute.ocrbackend.repository.LicensePlateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/license-plates")
@CrossOrigin(origins = "http://localhost:3000")
public class LicensePlateController {

    @Autowired
    private LicensePlateRepository licensePlateRepository;

    /**
     * GET /api/license-plates
     * – Returnează toate plăcuțele din baza de date
     * – Rol accesibil: POLICE
     */
    @PreAuthorize("hasRole('POLICE')")
    @GetMapping
    public List<LicensePlate> getAllPlates() {
        return licensePlateRepository.findAll();
    }

    /**
     * GET /api/license-plates/{plateNumber}
     * – Returnează listele de LicensePlate care au exactly plateNumber
     * – Rol accesibil: POLICE
     */
    @PreAuthorize("hasRole('POLICE')")
    @GetMapping("/{plateNumber}")
    public List<LicensePlate> getByPlateNumber(@PathVariable String plateNumber) {
        return licensePlateRepository.findByPlateNumber(plateNumber);
    }

    /**
     * POST /api/license-plates
     * – Salvează o entitate LicensePlate nouă (body conține JSON cu plateNumber + imagePath + brand/model/owner)
     * – Rol accesibil: POLICE, PARKING
     */
    @PreAuthorize("hasAnyRole('POLICE', 'PARKING')")
    @PostMapping
    public LicensePlate savePlate(@RequestBody LicensePlate plate) {
        // detectedAt se setează în constructorul entității, dacă este null.
        if (plate.getDetectedAt() == null) {
            plate.setDetectedAt(LocalDateTime.now());
        }

        return licensePlateRepository.save(plate);
    }

    /**
     * PUT /api/license-plates/{id}
     * – Actualizează brand, model și owner pentru LicensePlate cu id-ul dat
     * – Rol accesibil: POLICE, PARKING
     *
     * Așteaptă un JSON în body cu (orice subset din) { "brand": "...", "model": "...", "owner": "..." }
     * și reactualizează respectiv entitatea. Dacă entitatea nu există, răspunde 404 Not Found.
     */
    @PreAuthorize("hasAnyRole('POLICE', 'PARKING')")
    @PutMapping("/{id}")
    public ResponseEntity<LicensePlate> updatePlateDetails(
            @PathVariable Long id,
            @RequestBody LicensePlate updatedData) {

        Optional<LicensePlate> optionalPlate = licensePlateRepository.findById(id);
        if (!optionalPlate.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        LicensePlate existing = optionalPlate.get();

        // Actualizăm DOAR câmpurile brand, model și owner
        existing.setBrand(updatedData.getBrand());
        existing.setModel(updatedData.getModel());
        existing.setOwner(updatedData.getOwner());

        // Păstrăm detectArea și imagePath neschimbate. Dacă vrei, poți actualiza și ele tot aici.
        LicensePlate saved = licensePlateRepository.save(existing);
        return ResponseEntity.ok(saved);
    }
}
