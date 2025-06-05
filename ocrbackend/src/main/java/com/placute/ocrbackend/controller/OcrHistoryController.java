package com.placute.ocrbackend.controller;

import com.placute.ocrbackend.dto.OcrHistoryDto;
import com.placute.ocrbackend.model.OcrHistory;
import com.placute.ocrbackend.repository.OcrHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller pentru istoricul OCR:
 *  - GET /plates/history       → afișează toate înregistrările, ord. desc.
 *  - GET /plates/history/search → căutare fuzzy după plateNumber
 */
@RestController
@RequestMapping("/plates")
@CrossOrigin(origins = "http://localhost:3000")
public class OcrHistoryController {

    @Autowired
    private OcrHistoryRepository historyRepository;

    // 1) GET /plates/history
    @GetMapping("/history")
    public ResponseEntity<List<OcrHistoryDto>> getAllHistory() {
        List<OcrHistory> all = historyRepository.findAllByOrderByProcessedAtDesc();

        List<OcrHistoryDto> dtoList = all.stream()
                .map(h -> new OcrHistoryDto(
                        h.getId(),
                        h.getLicensePlate().getPlateNumber(),
                        h.getLicensePlate().getImagePath(),
                        h.getProcessedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    // 2) GET /plates/history/search?query=XYZ
    @GetMapping("/history/search")
    public ResponseEntity<List<OcrHistoryDto>> searchHistory(@RequestParam("query") String query) {
        List<OcrHistory> partial =
                historyRepository.findByLicensePlate_PlateNumberContainingIgnoreCaseOrderByProcessedAtDesc(query);

        List<OcrHistoryDto> dtoList = partial.stream()
                .map(h -> new OcrHistoryDto(
                        h.getId(),
                        h.getLicensePlate().getPlateNumber(),
                        h.getLicensePlate().getImagePath(),
                        h.getProcessedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }
}
