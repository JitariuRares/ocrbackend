package com.placute.ocrbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")              // aplică pe toate endpoint-urile “/**”
                        .allowedOrigins("*")            // permite orice origine
                        .allowedMethods("*")            // permite toate metodele: GET, POST, PUT, DELETE etc.
                        .allowedHeaders("*")            // permite toate antetele (headers)
                        .allowCredentials(false);       // dacă nu folosești cookies/token în cerere, poți seta false
                // Dacă totuși ai nevoie de credentiale (cookie, header Authorization etc.), setează true
            }
        };
    }
}
