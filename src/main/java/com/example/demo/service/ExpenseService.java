package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.demo.dto.ExpenseDTO;
import com.example.demo.entity.CategoryEntity;
import com.example.demo.entity.ExpenseEntity;
import com.example.demo.entity.ProfileEntity;
import com.example.demo.mapper.ExpenseMapper;
import com.example.demo.repository.CategoryRepo;
import com.example.demo.repository.ExpenseRepo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExpenseService {

	private final CategoryRepo categoryRepo;
	private final ExpenseRepo expenseRepo;
	private final ExpenseMapper mapper;
	private final ProfileService profileService;

	public ExpenseDTO addExpense(ExpenseDTO dto) {
		ProfileEntity proflie = profileService.getCurrentProfile();
		CategoryEntity category = categoryRepo.findById(dto.getCategoryId())
				.orElseThrow(() -> new RuntimeException("category not found"));
		ExpenseEntity newExpense = mapper.toEntity(dto, proflie, category);
		newExpense = expenseRepo.save(newExpense);
		return mapper.toDTO(newExpense);

	}

	// retervie all expenses for current month based on start and end date
	public List<ExpenseDTO> getCurrentMonthExpenseForCurrentUser() {
		ProfileEntity proflie = profileService.getCurrentProfile();
		LocalDate date = LocalDate.now();
		LocalDate startDate = date.withDayOfMonth(1);
		LocalDate endDate = date.withDayOfMonth(date.lengthOfMonth());
		List<ExpenseEntity> list = expenseRepo.findByProfileIdAndDateBetween(proflie.getId(), startDate, endDate);
		return list.stream().map(mapper::toDTO).toList();
	}
	
	//delete expense by id for current user
	public void deleteExpense(Long id) {
		ProfileEntity proflie = profileService.getCurrentProfile();
         ExpenseEntity entity = expenseRepo.findById(id)
        		 .orElseThrow(() -> new RuntimeException("expense not found"));
         if(!entity.getProfile().getId().equals(proflie.getId())) {
        	 throw new RuntimeException("unauthroized to delete Expense");
         }
         
         expenseRepo.delete(entity);
	}

	//get latest 5 expenses for currentUser
	public List<ExpenseDTO> getLatest5ExpensesForCurrentUser(){
		ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> list = expenseRepo.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(mapper :: toDTO).toList();
	}
	
	//GET total expenses of current user
	public BigDecimal totalExpenseForCurrentUser() {
		ProfileEntity profile = profileService.getCurrentProfile();
		BigDecimal total = expenseRepo.findTotalExpenseByProfileId(profile.getId());
	    return total != null ? total : BigDecimal.ZERO;
	}
	
	//filter Expenses
	public List<ExpenseDTO> filterExpense(LocalDate startDate,LocalDate endDate,String name,Sort sort){
		ProfileEntity profile = profileService.getCurrentProfile();
         List<ExpenseEntity> list = expenseRepo.findByProfileIdAndDateBetweenAndNameContainigIgnoreCase(profile.getId(), startDate, endDate, name, sort);
	     return list.stream().map(mapper :: toDTO).toList();
	}
	
	//notifications
	public List<ExpenseDTO> getExpenseForUserDate(Long profileId,LocalDate date){
	List<ExpenseEntity> list = expenseRepo.findByProfileIdAndDate(profileId, date);
	return list.stream().map(mapper :: toDTO).toList();
	}
	
}
