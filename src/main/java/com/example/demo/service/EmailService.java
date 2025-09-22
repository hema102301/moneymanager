package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailService {
	
	
	private final JavaMailSender mailSender;
	
	@Value("${spring.mail.from}")
	private String fromEmail;
	
	public void sendEmail(String to,String subject,String body) {
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setFrom(fromEmail);
			msg.setTo(to);
			msg.setSubject(subject);
			msg.setText(body);
			mailSender.send(msg);
			
		}catch(Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
