package com.placute.ocrbackend.controller;

import com.placute.ocrbackend.service.OcrService;
import com.placute.ocrbackend.repository.LicensePlateRepository;
import com.placute.ocrbackend.model.LicensePlate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class OcrController {

    @Autowired
    private OcrService ocrService;

    @Autowired
    private LicensePlateRepository plateRepository;

    /**
     * POST /api/ocr
     * – primește parametru “image” în form-data,
     * – apelează recognizeText(...) care salvează LicensePlate + OcrHistory
     * – returnează mesajul cu “Plăcuță detectată și salvată: XXX”
     */
    @PreAuthorize("hasAnyRole('POLICE', 'PARKING')")
    @PostMapping("/ocr")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);

        // recognizeText salvează și în LicensePlate, și în OcrHistory
        String result = ocrService.recognizeText(convFile);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/plates
     * – returnează lista tuturor plăcuțelor salvate (LicensePlate)
     */
    @PreAuthorize("hasAnyRole('POLICE', 'PARKING')")
    @GetMapping("/plates")
    public ResponseEntity<List<LicensePlate>> getAllPlates() {
        List<LicensePlate> plates = plateRepository.findAll();
        return ResponseEntity.ok(plates);
    }
}
