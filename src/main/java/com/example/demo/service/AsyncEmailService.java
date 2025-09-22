package com.example.demo.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncEmailService {

    private final EmailService emailService;

    public AsyncEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    public void sendActivationEmail(String to, String activationLink) {
        String subject = "Activate Your Money Manager Account";
        String body = "Click the following link to activate your account: " + activationLink;
        emailService.sendEmail(to, subject, body);
    }
}

