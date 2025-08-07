package com.placute.ocrbackend.service;

import com.placute.ocrbackend.model.LicensePlate;
import com.placute.ocrbackend.model.OcrHistory;
import com.placute.ocrbackend.repository.LicensePlateRepository;
import com.placute.ocrbackend.repository.OcrHistoryRepository;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OcrService {

    @Autowired
    private LicensePlateRepository plateRepository;

    @Autowired
    private OcrHistoryRepository historyRepository;

    private File preprocessImage(File inputFile) throws IOException {
        BufferedImage image = ImageIO.read(inputFile);
        BufferedImage gray = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g = gray.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        File output = new File(System.getProperty("java.io.tmpdir"), "preprocessed.png");
        ImageIO.write(gray, "png", output);
        return output;
    }

    public String recognizeText(File imageFile) {
        try {
            File processed = preprocessImage(imageFile);

            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
            tesseract.setLanguage("eng");

            String rawText = tesseract.doOCR(processed);
            String cleanedText = rawText.toUpperCase().replaceAll("[^A-Z0-9 ]", "");

            Pattern pattern = Pattern.compile("[A-Z]{1,2}\\s?\\d{2}\\s?[A-Z]{3}");
            Matcher matcher = pattern.matcher(cleanedText);

            if (matcher.find()) {
                String plate = matcher.group().replaceAll("\\s+", "");
                List<LicensePlate> existing = plateRepository.findByPlateNumber(plate);
                LicensePlate lp;
                if (existing.isEmpty()) {
                    lp = new LicensePlate(plate, imageFile.getAbsolutePath());
                    plateRepository.save(lp);
                } else {
                    lp = existing.get(0);
                }

                OcrHistory history = new OcrHistory(lp, imageFile.getName(), LocalDateTime.now());
                historyRepository.save(history);

                return "Placuta detectata si salvata: " + plate;
            } else {
                return "Nicio placuta valida gasita.";
            }

        } catch (TesseractException | IOException e) {
            return "Eroare la OCR: " + e.getMessage();
        }
    }

    public LicensePlate recognizeAndReturnPlate(File imageFile) {
        try {
            File processed = preprocessImage(imageFile);

            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
            tesseract.setLanguage("eng");

            String rawText = tesseract.doOCR(processed);
            System.out.println("TEXT DETECTAT de OCR:\n" + rawText);
            String cleanedText = rawText.toUpperCase().replaceAll("[^A-Z0-9 ]", "");

            Pattern pattern = Pattern.compile("[A-Z]{1,2}\\s?\\d{2}\\s?[A-Z]{3}");
            Matcher matcher = pattern.matcher(cleanedText);

            if (!matcher.find()) {
                return null;
            }

            String plate = matcher.group().replaceAll("\\s+", "");
            List<LicensePlate> existing = plateRepository.findByPlateNumber(plate);
            LicensePlate lp;
            if (existing.isEmpty()) {
                lp = new LicensePlate(plate, imageFile.getAbsolutePath());
                plateRepository.save(lp);
            } else {
                lp = existing.get(0);
            }

            OcrHistory history = new OcrHistory(lp, imageFile.getName(), LocalDateTime.now());
            historyRepository.save(history);

            return lp;

        } catch (TesseractException | IOException e) {
            throw new RuntimeException("Eroare la OCR: " + e.getMessage());
        }
    }
}
