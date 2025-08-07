package com.placute.ocrbackend.service;

import com.placute.ocrbackend.auth.AuthResponse;
import com.placute.ocrbackend.dto.LoginRequest;
import com.placute.ocrbackend.dto.RegisterRequest;
import com.placute.ocrbackend.model.AppUser;
import com.placute.ocrbackend.repository.AppUserRepository;
import com.placute.ocrbackend.security.JwtService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String register(RegisterRequest request) {
        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);
        return "User inregistrat cu succes!";
    }

    public AuthResponse login(LoginRequest request) {
        AppUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Userul nu exista"));

        boolean valid = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!valid) {
            throw new RuntimeException("Parola incorecta");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
