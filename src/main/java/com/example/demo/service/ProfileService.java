package com.example.demo.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthDTO;
import com.example.demo.dto.ProfileDto;
import com.example.demo.entity.ProfileEntity;
import com.example.demo.mapper.ProfileMapper;
import com.example.demo.repository.ProfileRepo;
import com.example.demo.util.Jwtutil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProfileService {
	
	private final ProfileRepo profileRepo;
	private final ProfileMapper mapper;
	private final EmailService emailService;
	private final AuthenticationManager authManager;
	private final Jwtutil jwtutil;
	
	@Value("{$app.activation.url}")
	private String activationUrl;
	
	public ProfileDto registerProfile(ProfileDto dto) {
		ProfileEntity newProfile = mapper.toEntity(dto);
		newProfile.setActivationToken(UUID.randomUUID().toString());
		newProfile = profileRepo.save(newProfile);
		String activationlink = activationUrl+"/api/v1.0/activate?token="+newProfile.getActivationToken();
		String subject ="Active Your Money Manager account";
		String body = "Click on the following link to activate your account: "+activationlink;
		emailService.sendEmail(newProfile.getEmail(), subject, body);
		return mapper.toDto(newProfile);
		
	}
	
	//validate the token
	
	public boolean activateProfile(String activationToken) {
		return profileRepo.findByActivationToken(activationToken)
				.map(profile ->{
					profile.setIsActive(true);
					profileRepo.save(profile);
					return true;
				})
				.orElse(false);
	}

public boolean isAccountActive(String email) {
		return profileRepo.findByEmail(email)
				.map(ProfileEntity::getIsActive)
				.orElse(false);
	}

 public ProfileEntity getCurrentProfile() {
	 Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
	return profileRepo.findByEmail(authentication.getName())
			.orElseThrow(() -> new UsernameNotFoundException("Profile not found with email: "+authentication.getName()));	 
 }
 
 public ProfileDto getPublicProfile(String email) {
	 ProfileEntity currentUser = null;
	 if(email == null) {
		 currentUser = getCurrentProfile();
	 }else {
		 currentUser = profileRepo.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("Profile not found with email: "+email));	 
	 }	 
	// System.out.println( currentUser.getEmail());
	 return mapper.toDto(currentUser);
 }

public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO) {
	try {
		authManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(),authDTO.getPassword()));
	     String token = jwtutil.generateToken(authDTO.getEmail());
	     System.out.println(token);
		return Map.of(
	    		 "token",token,
	    		 "user",getPublicProfile(authDTO.getEmail())    		 
	    		 );
	
	}catch(Exception e) {
		e.printStackTrace();  // ✅ logs exact cause
        System.out.println("Authentication failed: " + e.getMessage()); 
		throw new RuntimeException("Invalid email or Password");
	}

 
}
}
