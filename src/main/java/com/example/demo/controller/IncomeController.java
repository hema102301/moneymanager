package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.IncomeDTO;
import com.example.demo.service.IncomeService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/incomes")
public class IncomeController {
	
	private final IncomeService service;

	
	@PostMapping
	public ResponseEntity<IncomeDTO> addincome(@RequestBody IncomeDTO dto){
		IncomeDTO savedExpense =  service.addIncome(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
	}
	
	@GetMapping
	public ResponseEntity<List<IncomeDTO>> getincomesForCurrentUser(){
		List<IncomeDTO> currentMonthExpense  =  service.getCurrentMonthIncomeForCurrentUser();
		return ResponseEntity.status(HttpStatus.OK).body(currentMonthExpense);

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteincome(@PathVariable Long id){
		service.deleteIncome(id);
		return ResponseEntity.noContent().build();
	}
	
	 @GetMapping("/latest5")
		public ResponseEntity<List<IncomeDTO>> getTop5IncomeForCurrentUser(){
			List<IncomeDTO> latestExpense = service.getLatest5incomesForCurrentUser();
			return ResponseEntity.status(HttpStatus.OK).body(latestExpense);
		}
		
		@GetMapping("/total")
		public ResponseEntity<BigDecimal> getSumOfTotalExpense(){
			BigDecimal total = service.totalIncomeForCurrentUser();
			return ResponseEntity.status(HttpStatus.OK).body(total);
		}

}
