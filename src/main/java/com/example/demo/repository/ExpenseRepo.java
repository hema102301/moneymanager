package com.example.demo.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.ExpenseEntity;

public interface ExpenseRepo extends JpaRepository<ExpenseEntity,Long>{
              List<ExpenseEntity> findByProfileIdOrderByDateDesc(Long profileId);
              
              List<ExpenseEntity>  findTop5ByProfileIdOrderByDateDesc(Long profileId);
              
              @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.profile.id = :profileId")
             BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);
              
           @Query("SELECT e FROM ExpenseEntity e WHERE e.profile.id = :profileId AND e.date BETWEEN :startDate AND :endDate AND LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
           List<ExpenseEntity>   findByProfileIdAndDateBetweenAndNameContainigIgnoreCase(
            		  Long profileId,
            		  LocalDate startDate,
            		  LocalDate endDate,
            		  String name,
            		  Sort sort
            		  );
           
           @Query("SELECT e FROM ExpenseEntity e WHERE e.profile.id = :profileId AND e.date BETWEEN :startDate AND :endDate") 
          List<ExpenseEntity> findByProfileIdAndDateBetween(Long profileId,LocalDate startDate,LocalDate endDate);

           @Query("SELECT e FROM ExpenseEntity e WHERE e.profile.id = :profileId AND e.date = :date")
           List<ExpenseEntity> findByProfileIdAndDate(@Param("profileId") Long profileId,
                                                      @Param("date") LocalDate date);


}
