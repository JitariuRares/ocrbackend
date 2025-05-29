package com.placute.ocrbackend.controller;

import com.placute.ocrbackend.model.AppUser;
import com.placute.ocrbackend.model.UserRole;
import com.placute.ocrbackend.repository.AppUserRepository;
import com.placute.ocrbackend.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AppUserRepository appUserRepository;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<?> getDashboard(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("role", user.getRole());

        switch (user.getRole()) {
            case POLICE -> response.put("message",
                    "Access all vehicle and user data. This includes full license plate recognition results, vehicle records (brand, model, ITP), and OCR scan history.");
            case INSURANCE -> response.put("message",
                    "View ITP and damage history for all vehicles. This includes access to validated insurance data.");
            case PARKING -> response.put("message",
                    "Track parking entries and exits. View validations and time spent in parking areas.");
        }

        return ResponseEntity.ok(response);
    }
}
