package com.placute.ocrbackend.repository;

import com.placute.ocrbackend.model.OcrHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository pentru OcrHistory:
 *   - findAllByOrderByProcessedAtDesc(): listează toate intrările, ordonate descrescător după processedAt
 *   - findByLicensePlate_PlateNumberContainingIgnoreCaseOrderByProcessedAtDesc(): fuzzy search după plateNumber
 */
public interface OcrHistoryRepository extends JpaRepository<OcrHistory, Long> {

    List<OcrHistory> findAllByOrderByProcessedAtDesc();

    List<OcrHistory> findByLicensePlate_PlateNumberContainingIgnoreCaseOrderByProcessedAtDesc(String fragment);
}
