package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.entity.CategoryEntity;
import com.example.demo.entity.ProfileEntity;

@Component
public class CategoryMapper {

	
	
	public CategoryEntity toEntity(CategoryDTO dto,ProfileEntity profile) {
		return CategoryEntity.builder()
				.name(dto.getName())
				.icon(dto.getIcon())
				.profile(profile)
				.type(dto.getType())
				.build();
	}
	
	public CategoryDTO toDTO(CategoryEntity entity) {
		return CategoryDTO.builder()
				.id(entity.getId())
				.profileId(entity.getProfile() != null ? entity.getProfile().getId() : null)
				.name(entity.getName())
				.icon(entity.getIcon())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.type(entity.getType())
				.build();
	}
}
