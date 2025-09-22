package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.IncomeDTO;
import com.example.demo.entity.CategoryEntity;
import com.example.demo.entity.IncomeEntity;
import com.example.demo.entity.ProfileEntity;

@Component
public class IncomeMapper {
	
	public IncomeEntity toEntity(IncomeDTO dto,ProfileEntity profile,CategoryEntity category) {
		return IncomeEntity.builder()
				.name(dto.getName())
				.icon(dto.getIcon())
				.amount(dto.getAmount())
				.date(dto.getDate())
				.profile(profile)
				.category(category)
				.build();
	}
	
	public IncomeDTO toDTO(IncomeEntity entity) {
		return IncomeDTO.builder()
				.id(entity.getId())
				.name(entity.getName())
				.icon(entity.getIcon())
				.categoryId(entity.getCategory() != null ?entity.getCategory().getId(): null)
				.categoryName(entity.getCategory()!= null ?entity.getCategory().getName(): null)
				.amount(entity.getAmount())
				.date(entity.getDate())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.build();
	}


}
