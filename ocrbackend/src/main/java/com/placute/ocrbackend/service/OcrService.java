package com.placute.ocrbackend.service;

import com.placute.ocrbackend.model.LicensePlate;
import com.placute.ocrbackend.model.OcrHistory;
import com.placute.ocrbackend.repository.LicensePlateRepository;
import com.placute.ocrbackend.repository.OcrHistoryRepository;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OcrService {

    @Autowired
    private LicensePlateRepository plateRepository;

    @Autowired
    private OcrHistoryRepository historyRepository;

    /**
     * Preprocesează imaginea: transformă la alb-negru (binary).
     * @param inputFile fișierul original
     * @return fișierul temporar preprocesat
     */
    private File preprocessImage(File inputFile) throws IOException {
        BufferedImage image = ImageIO.read(inputFile);

        // Convertim la alb-negru
        BufferedImage gray = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g = gray.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        // Salvăm în folderul temporar un fișier "preprocessed.png"
        File output = new File(System.getProperty("java.io.tmpdir"), "preprocessed.png");
        ImageIO.write(gray, "png", output);
        return output;
    }

    /**
     * Metoda principală apelată din controller:
     *  - primește un fișier imagine (File),
     *  - îl preprocesează,
     *  - rulează Tesseract,
     *  - curăță textul,
     *  - caută pattern-ul plăcuței,
     *  - salvează un obiect LicensePlate nou (dacă nu exista),
     *  - apoi salvează un OcrHistory care referă LicensePlate-ul.
     */
    public String recognizeText(File imageFile) {
        try {
            // 1) Preprocesare (alb-negru etc.)
            File processed = preprocessImage(imageFile);

            // 2) Configurare Tesseract
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
            tesseract.setLanguage("eng");

            // 3) OCR propriu-zis
            String rawText = tesseract.doOCR(processed);
            System.out.println("Text OCR brut:\n" + rawText);

            // 4) Curățăm textul: litere majuscule și cifre
            String cleanedText = rawText.toUpperCase().replaceAll("[^A-Z0-9 ]", "");
            System.out.println("Text curățat:\n" + cleanedText);

            // 5) Căutăm pattern-ul specific plăcuței:
            //    două litere (sau una), două cifre, trei litere, eventual cu spații
            Pattern pattern = Pattern.compile("[A-Z]{1,2}\\s?\\d{2}\\s?[A-Z]{3}");
            Matcher matcher = pattern.matcher(cleanedText);

            if (matcher.find()) {
                // 6) Extragem plăcuța fără spații
                String plate = matcher.group().replaceAll("\\s+", "");
                System.out.println("Plăcuță găsită: " + plate);

                // 7) Salvăm sau preluăm LicensePlate din DB
                List<LicensePlate> existing = plateRepository.findByPlateNumber(plate);
                LicensePlate lp;
                if (existing.isEmpty()) {
                    // nu exista în DB → creăm unul nou
                    lp = new LicensePlate(plate, imageFile.getAbsolutePath());
                    plateRepository.save(lp);
                } else {
                    // existau deja (dacă mai există “istoric” de salvări multiple),
                    // luăm prima intrare (dacă știi că plateNumber e unic, de obicei list size == 1)
                    lp = existing.get(0);
                }

                // 8) Creăm un OcrHistory care referă LicensePlate-ul
                OcrHistory history = new OcrHistory(
                        lp,
                        imageFile.getName(),           // poți salva doar numele fișierului
                        LocalDateTime.now()
                );
                historyRepository.save(history);

                return "Plăcuță detectată și salvată: " + plate;
            } else {
                return "Nicio plăcuță validă găsită.";
            }

        } catch (TesseractException | IOException e) {
            return "Eroare la OCR: " + e.getMessage();
        }
    }
}
