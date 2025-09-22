package com.example.demo.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.demo.entity.IncomeEntity;

public interface IncomeRepo extends JpaRepository<IncomeEntity,Long> {
	
	 List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileId);
     
     List<IncomeEntity>  findTop5ByProfileIdOrderByDateDesc(Long profileId);
     
     @Query("SELECT SUM(e.amount) FROM IncomeEntity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalIncomeByProfileId(@Param("profileId") Long profileId);
     
  @Query("SELECT e FROM IncomeEntity e WHERE e.profile.id = :profileId AND e.date BETWEEN :startDate AND :endDate AND LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
  List<IncomeEntity>   findByProfileIdAndDateBetweenAndNameContainigIgnoreCase(
   		  Long profileId,
   		  LocalDate startDate,
   		  LocalDate endDate,
   		  String name,
   		  Sort sort
   		  );
  
  @Query("SELECT e FROM IncomeEntity e WHERE e.profile.id = :profileId AND e.date BETWEEN :startDate AND :endDate") 
 List<IncomeEntity> findByProfileIdAndDateBetween(Long profileId,LocalDate startDate,LocalDate endDate);

}
