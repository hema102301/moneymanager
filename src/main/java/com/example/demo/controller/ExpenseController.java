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
import com.example.demo.dto.ExpenseDTO;
import com.example.demo.service.ExpenseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/expense")
public class ExpenseController {
	
	private final ExpenseService service;
	
	@PostMapping
	public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO dto){
		ExpenseDTO savedExpense =  service.addExpense(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
	}
	
	@GetMapping
	public ResponseEntity<List<ExpenseDTO>> getExpensesForCurrentUser(){
		List<ExpenseDTO> currentMonthExpense  =  service.getCurrentMonthExpenseForCurrentUser();
		return ResponseEntity.status(HttpStatus.OK).body(currentMonthExpense);

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteexpense(@PathVariable Long id){
		service.deleteExpense(id);
		return ResponseEntity.noContent().build();
	}
	
	 @GetMapping("/latest5")
	public ResponseEntity<List<ExpenseDTO>> getTop5ExpenseForCurrentUser(){
		List<ExpenseDTO> latestExpense = service.getLatest5ExpensesForCurrentUser();
		return ResponseEntity.status(HttpStatus.OK).body(latestExpense);
	}
	
	@GetMapping("/total")
	public ResponseEntity<BigDecimal> getSumOfTotalExpense(){
		BigDecimal total = service.totalExpenseForCurrentUser();
		return ResponseEntity.status(HttpStatus.OK).body(total);
	}
}
