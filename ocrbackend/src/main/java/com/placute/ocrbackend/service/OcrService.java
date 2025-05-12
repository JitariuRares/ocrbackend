package com.placute.ocrbackend.service;

import com.placute.ocrbackend.model.LicensePlate;
import com.placute.ocrbackend.repository.LicensePlateRepository;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OcrService {

    @Autowired
    private LicensePlateRepository plateRepository;

    private File preprocessImage(File inputFile) throws IOException {
        BufferedImage image = ImageIO.read(inputFile);

        // Convertim imaginea la alb-negru
        BufferedImage gray = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g = gray.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        // Salvează imaginea procesată într-un fișier temporar
        File output = new File(System.getProperty("java.io.tmpdir"), "preprocessed.png");
        ImageIO.write(gray, "png", output);
        return output;
    }

    public String recognizeText(File imageFile) {
        try {
            File processed = preprocessImage(imageFile); // imagine prelucrată

            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
            tesseract.setLanguage("eng");

            String rawText = tesseract.doOCR(processed);
            System.out.println("Text OCR brut:\n" + rawText);

            String cleanedText = rawText.toUpperCase().replaceAll("[^A-Z0-9 ]", "");
            System.out.println("Text curățat:\n" + cleanedText);

            Pattern pattern = Pattern.compile("[A-Z]{1,2}\\s?\\d{2}\\s?[A-Z]{3}");
            Matcher matcher = pattern.matcher(cleanedText);

            if (matcher.find()) {
                String plate = matcher.group().replaceAll("\\s+", "");

                // Salvare în baza de date
                LicensePlate lp = new LicensePlate(plate, imageFile.getAbsolutePath());
                plateRepository.save(lp);

                return "Plăcuță detectată și salvată: " + plate;
            } else {
                return "Nicio plăcuță validă găsită.";
            }

        } catch (TesseractException | IOException e) {
            return "Eroare la OCR: " + e.getMessage();
        }
    }
}
