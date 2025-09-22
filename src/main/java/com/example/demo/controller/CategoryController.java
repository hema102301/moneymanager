package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CategoryController {
	
	private final CategoryService categoryService;
	
	@PostMapping("/categories")
	public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO dto){
		CategoryDTO savedCategory = categoryService.saveCategory(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
	}
	
	@GetMapping("/getcategories")
	public ResponseEntity<List<CategoryDTO>> getCategories(){
		List<CategoryDTO> listOfCategories = categoryService.getCategoriesForCurrentUser();
		return ResponseEntity.status(HttpStatus.OK).body(listOfCategories);
	}
	
	@GetMapping("/getcategoriesbytype")
	public ResponseEntity<List<CategoryDTO>> getCategories(@RequestParam String type){
		System.out.println(type);
		List<CategoryDTO> listOfCategoriesByType = categoryService.getCategoriesByTypeForCurrentUser(type);
		for(CategoryDTO c : listOfCategoriesByType) {
			  System.out.println(c);
		  }
		return ResponseEntity.status(HttpStatus.OK).body(listOfCategoriesByType);
	}
	
	@PutMapping("/id/{id}")
	public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id,@RequestBody CategoryDTO dto){
		CategoryDTO updatedCategory = categoryService.updateCategory(id, dto);
		return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);

	}
	
}
