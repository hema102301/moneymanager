package com.example.demo.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demo.dto.CategoryDTO;
import com.example.demo.entity.CategoryEntity;
import com.example.demo.entity.ProfileEntity;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.repository.CategoryRepo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService {
	
	private final ProfileService profileService;
	private final CategoryRepo categorRepo;
	private final CategoryMapper mapper;
	
  public CategoryDTO saveCategory(CategoryDTO dto) {
	 ProfileEntity profile = profileService.getCurrentProfile();
	  if(categorRepo.existsByNameAndProfileId(dto.getName(),profile.getId())) {
		  throw new RuntimeException("Category with this name already exists");
	  }
	  
	  CategoryEntity newCategory = mapper.toEntity(dto,profile);
	  newCategory = categorRepo.save(newCategory);
	  return mapper.toDTO(newCategory);
  }
  
  //get categories for current use
  
  public List<CategoryDTO> getCategoriesForCurrentUser(){
	  ProfileEntity profile = profileService.getCurrentProfile();
	  List<CategoryEntity> categories = categorRepo.findByProfileId(profile.getId());
	  return categories.stream().map(mapper :: toDTO).toList();
  }
  
  public List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type){
	  System.out.println(type);
	  ProfileEntity profile = profileService.getCurrentProfile();
	  
	  System.out.println("Profile ID: " + profile.getId());
	    System.out.println("Type: " + type);

	  List<CategoryEntity> categoriesByType = categorRepo.findByTypeAndProfileId(type, profile.getId());
	  for(CategoryEntity c : categoriesByType) {
		  System.out.println(c);
	  }
	  
	  return categoriesByType.stream().map(mapper :: toDTO).toList();

  }
  
  public CategoryDTO  updateCategory(Long categoryId,CategoryDTO dto) {
	  ProfileEntity profile = profileService.getCurrentProfile();
	 CategoryEntity existingCategory = categorRepo.findByIdAndProfileId(categoryId, profile.getId())
			 .orElseThrow(() -> new RuntimeException("Category not found and not acessible"));
	 existingCategory.setName(dto.getName());
	 existingCategory.setIcon(dto.getIcon());
	 existingCategory = categorRepo.save(existingCategory);
	 
	 return mapper.toDTO(existingCategory);
  }

}
