package com.placute.ocrbackend.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.placute.ocrbackend.model.Insurance;
import com.placute.ocrbackend.model.LicensePlate;
import com.placute.ocrbackend.model.ParkingHistory;
import com.placute.ocrbackend.repository.InsuranceRepository;
import com.placute.ocrbackend.repository.LicensePlateRepository;
import com.placute.ocrbackend.repository.ParkingHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/license-plates")
@CrossOrigin(origins = "http://localhost:3000")
public class LicensePlateController {

    @Autowired
    private LicensePlateRepository licensePlateRepository;

    @Autowired
    private InsuranceRepository insuranceRepository;

    @Autowired
    private ParkingHistoryRepository parkingHistoryRepository;

    @PreAuthorize("hasRole('POLICE')")
    @GetMapping
    public List<LicensePlate> getAllPlates() {
        return licensePlateRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('POLICE', 'INSURANCE')")
    @GetMapping("/{plateNumber}")
    public List<LicensePlate> getByPlateNumber(@PathVariable String plateNumber) {
        return licensePlateRepository.findByPlateNumber(plateNumber);
    }

    @PreAuthorize("hasAnyRole('POLICE', 'PARKING')")
    @PostMapping
    public ResponseEntity<?> savePlate(@RequestBody LicensePlate plate) {
        String plateNumber = plate.getPlateNumber();
        String imagePath = plate.getImagePath();

        List<LicensePlate> existingPlates = licensePlateRepository.findByPlateNumber(plateNumber);
        boolean alreadyExists = existingPlates.stream().anyMatch(p ->
                (p.getImagePath() == null && imagePath == null) ||
                        (p.getImagePath() != null && p.getImagePath().equals(imagePath))
        );

        if (alreadyExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Plăcuța există deja în sistem.");
        }

        if (plate.getDetectedAt() == null) {
            plate.setDetectedAt(LocalDateTime.now());
        }

        LicensePlate saved = licensePlateRepository.save(plate);
        return ResponseEntity.ok(saved);
    }


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
        existing.setBrand(updatedData.getBrand());
        existing.setModel(updatedData.getModel());
        existing.setOwner(updatedData.getOwner());

        LicensePlate saved = licensePlateRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasRole('POLICE')")
    @GetMapping("/pdf/{plateNumber}")
    public ResponseEntity<byte[]> exportPdf(@PathVariable String plateNumber) throws IOException {
        List<LicensePlate> plates = licensePlateRepository.findByPlateNumber(plateNumber);
        if (plates.isEmpty()) {
            throw new RuntimeException("Plăcuța nu există");
        }
        LicensePlate plate = plates.get(plates.size() - 1);

        List<Insurance> insurances = insuranceRepository.findByLicensePlate_PlateNumber(plateNumber);
        List<ParkingHistory> parking = parkingHistoryRepository.findByLicensePlate_PlateNumber(plateNumber);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Fisa completa pentru placuta: " + plateNumber));
            document.add(new Paragraph(" "));

            if (plate.getImagePath() != null) {
                try {
                    Image img = Image.getInstance(plate.getImagePath());
                    img.scaleToFit(400, 200);
                    document.add(img);
                } catch (Exception e) {
                    document.add(new Paragraph("[Imagine indisponibila]"));
                }
            }

            document.add(new Paragraph("Marca: " + (plate.getBrand() != null ? plate.getBrand() : "-")));
            document.add(new Paragraph("Model: " + (plate.getModel() != null ? plate.getModel() : "-")));
            document.add(new Paragraph("Proprietar: " + (plate.getOwner() != null ? plate.getOwner() : "-")));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Asigurari:"));
            if (insurances.isEmpty()) {
                document.add(new Paragraph(" - Nicio asigurare gasita"));
            } else {
                for (Insurance ins : insurances) {
                    document.add(new Paragraph(" - " + ins.getCompany() +
                            " (" + ins.getValidFrom() + " → " + ins.getValidTo() + ")"));
                }
            }
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Istoric parcare:"));
            if (parking.isEmpty()) {
                document.add(new Paragraph(" - Niciun istoric de parcare"));
            } else {
                for (ParkingHistory entry : parking) {
                    document.add(new Paragraph(" - Intrare: " + entry.getEntryTime() +
                            ", Iesire: " + (entry.getExitTime() != null ? entry.getExitTime() : "N/A")));
                }
            }

        } catch (DocumentException e) {
            throw new IOException("Eroare la generarea PDF-ului: " + e.getMessage());
        } finally {
            document.close();
        }

        byte[] pdfBytes = out.toByteArray();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=plate_" + plateNumber + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
