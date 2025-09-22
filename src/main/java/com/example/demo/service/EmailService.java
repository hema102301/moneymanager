package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${spring.mail.from}")
    private String fromEmail;

    private final RestTemplate restTemplate;

    @Async
    public void sendEmail(String to, String subject, String body) {
        try {
            Map<String, Object> emailRequest = Map.of(
                "sender", Map.of("name", "Money Manager", "email", fromEmail),
                "to", List.of(Map.of("email", to)),
                "subject", subject,
                "htmlContent", body
            );

            HttpHeaders headers = new HttpHeaders();
            headers.set("api-key", apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(emailRequest, headers);

            restTemplate.postForEntity("https://api.brevo.com/v3/smtp/email", request, String.class);

        } catch (Exception e) {
            // Log error, but do NOT fail registration
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
