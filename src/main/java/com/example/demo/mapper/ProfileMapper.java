package com.example.demo.mapper;


import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Component;

import com.example.demo.dto.ProfileDto;
import com.example.demo.entity.ProfileEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ProfileMapper {
	
	private final PasswordEncoder encoder;
	
	public ProfileEntity toEntity(ProfileDto dto) {
		
		return ProfileEntity.builder()
				.id(dto.getId())
				.fullName(dto.getFullName())
				.email(dto.getEmail())
				.password(encoder.encode(dto.getPassword()))
				.profileImageUrl(dto.getProfileImageUrl())
				.createdAt(dto.getCreatedAt())
				.updatedAt(dto.getUpdatedAt())
				.build();
		
	}
	
	
	public ProfileDto toDto(ProfileEntity entity) {
		return ProfileDto.builder()
				.id(entity.getId())
				.fullName(entity.getFullName())
				.email(entity.getEmail())
				.profileImageUrl(entity.getProfileImageUrl())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.build();
	}

}
