package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthDTO;
import com.example.demo.dto.ProfileDto;
import com.example.demo.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ProfileController {
	
	private final ProfileService service;
	
	@PostMapping("/register")
	public ResponseEntity<ProfileDto> registerProfile(@RequestBody ProfileDto dto){
		
		ProfileDto registeredProfile = service.registerProfile(dto);
		return new ResponseEntity<>(registeredProfile,HttpStatus.CREATED);
		
	}
	
	@GetMapping("/activate")
	public ResponseEntity<String> activateProfile(@RequestParam String token){
		boolean isActivated = service.activateProfile(token);
		if(isActivated) {
			return ResponseEntity.ok("Profile activated sucessfully");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token not found or Already used");
		}		
	}
	
	@PostMapping("/login")
	public ResponseEntity<Map<String,Object>> login(@RequestBody AuthDTO authDTO){
		try {
			if(!service.isAccountActive(authDTO.getEmail())) {				
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Account is not active.Please activate your account"));
			}
			
			Map<String,Object> response = service.authenticateAndGenerateToken(authDTO);
			return ResponseEntity.ok(response);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
					"message",e.getMessage()
					));
		}
	}


}
