package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.demo.dto.IncomeDTO;
import com.example.demo.entity.CategoryEntity;
import com.example.demo.entity.IncomeEntity;
import com.example.demo.entity.ProfileEntity;
import com.example.demo.mapper.IncomeMapper;
import com.example.demo.repository.CategoryRepo;
import com.example.demo.repository.IncomeRepo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class IncomeService {
	
	private final CategoryRepo categoryRepo;
	private final IncomeRepo incomeRepo;
	private final IncomeMapper mapper;
	private final ProfileService profileService;

	public IncomeDTO addIncome(IncomeDTO dto) {
		ProfileEntity proflie = profileService.getCurrentProfile();
		CategoryEntity category = categoryRepo.findById(dto.getCategoryId())
				.orElseThrow(() -> new RuntimeException("category not found"));
		IncomeEntity newExpense = mapper.toEntity(dto,proflie,category);
		newExpense = incomeRepo.save(newExpense);
		return mapper.toDTO(newExpense);
		
	}
	
	// retervie all expenses for current month based on start and end date
		public List<IncomeDTO> getCurrentMonthIncomeForCurrentUser() {
			ProfileEntity proflie = profileService.getCurrentProfile();
			LocalDate date = LocalDate.now();
			LocalDate startDate = date.withDayOfMonth(1);
			LocalDate endDate = date.withDayOfMonth(date.lengthOfMonth());
			List<IncomeEntity> list = incomeRepo.findByProfileIdAndDateBetween(proflie.getId(), startDate, endDate);
			return list.stream().map(mapper::toDTO).toList();
		}

		//delete expense by id for current user
		public void deleteIncome(Long id) {
			ProfileEntity proflie = profileService.getCurrentProfile();
	         IncomeEntity entity = incomeRepo.findById(id)
	        		 .orElseThrow(() -> new RuntimeException("expense not found"));
	         if(!entity.getProfile().getId().equals(proflie.getId())) {
	        	 throw new RuntimeException("unauthroized to delete Expense");
	         }
	         
	         incomeRepo.delete(entity);
		}
		
		//get latest 5 expenses for currentUser
		public List<IncomeDTO> getLatest5incomesForCurrentUser(){
			ProfileEntity profile = profileService.getCurrentProfile();
	        List<IncomeEntity> list = incomeRepo.findTop5ByProfileIdOrderByDateDesc(profile.getId());
	        return list.stream().map(mapper :: toDTO).toList();
		}
		
		//GET total expenses of current user
		public BigDecimal totalIncomeForCurrentUser() {
			ProfileEntity profile = profileService.getCurrentProfile();
			BigDecimal total = incomeRepo.findTotalIncomeByProfileId(profile.getId());
		    return total != null ? total : BigDecimal.ZERO;
		}
		
		//filter Expenses
		public List<IncomeDTO> filterIncome(LocalDate startDate,LocalDate endDate,String name,Sort sort){
			ProfileEntity profile = profileService.getCurrentProfile();
	         List<IncomeEntity> list = incomeRepo.findByProfileIdAndDateBetweenAndNameContainigIgnoreCase(profile.getId(), startDate, endDate, name, sort);
		     return list.stream().map(mapper :: toDTO).toList();
		}

}
