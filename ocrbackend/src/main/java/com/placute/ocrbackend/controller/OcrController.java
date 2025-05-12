package com.placute.ocrbackend.controller;

import com.placute.ocrbackend.model.LicensePlate;
import com.placute.ocrbackend.repository.LicensePlateRepository;
import com.placute.ocrbackend.service.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OcrController {

    @Autowired
    private OcrService ocrService;

    @Autowired
    private LicensePlateRepository plateRepository;

    @PostMapping("/ocr")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);
        String result = ocrService.recognizeText(convFile);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/plates")
    public ResponseEntity<List<LicensePlate>> getAllPlates() {
        List<LicensePlate> plates = plateRepository.findAll();
        return ResponseEntity.ok(plates);
    }
}
