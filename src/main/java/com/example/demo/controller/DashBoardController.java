package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/dashboard")
public class DashBoardController {
	
	private final DashboardService service;
	
	@GetMapping
	public ResponseEntity<Map<String,Object>> getDashBoardData(){
		Map<String,Object> dashBoardData = service.getDashBoardData();
		return ResponseEntity.ok(dashBoardData);
	}

}
