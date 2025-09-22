package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.demo.entity.CategoryEntity;

@EnableJpaRepositories
public interface CategoryRepo extends JpaRepository<CategoryEntity,Long> {

	List<CategoryEntity> findByProfileId(Long profileId);
	
	Optional<CategoryEntity> findByIdAndProfileId(Long id,Long profileId);
	
	List<CategoryEntity> findByTypeAndProfileId(String type,Long profileId);
	
	Boolean existsByNameAndProfileId(String name,Long profileId);
}
