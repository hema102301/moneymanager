package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ExpenseDTO;
import com.example.demo.dto.IncomeDTO;
import com.example.demo.entity.FilterDTO;
import com.example.demo.service.ExpenseService;
import com.example.demo.service.IncomeService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/filter")
@RequiredArgsConstructor
@RestController
public class FilterController {
	
	private final ExpenseService expenseService;
	private final IncomeService incomeService;
	
	@PostMapping
	public ResponseEntity<?> filterTransactions(@RequestBody FilterDTO dto){
		LocalDate startDate = dto.getStartdate() != null ? dto.getStartdate() : LocalDate.now();
		LocalDate endDate = dto.getEnddate() != null ? dto.getEnddate() : LocalDate.now();
        String name = dto.getName() != null ? dto.getName() : "";
        String sortField = dto.getSortField() != null ? dto.getSortField() : "date";
        Sort.Direction direction = "desc".equalsIgnoreCase(dto.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
	    Sort sort = Sort.by(direction, sortField);
	    
	    if("income".equalsIgnoreCase(dto.getType())) {
	    List<IncomeDTO> incomes =	incomeService.filterIncome(startDate, endDate, name, sort);
	    return ResponseEntity.ok(incomes);
	    }else if("expense".equalsIgnoreCase(dto.getType())) {
		    List<ExpenseDTO> expenses =	expenseService.filterExpense(startDate, endDate, name, sort);
		    return ResponseEntity.ok(expenses);

	    }else {
	    	return ResponseEntity.badRequest().body("Invalid type. Must be 'Income' or 'Expense'");
	    }
	}

}
