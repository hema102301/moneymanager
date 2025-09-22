package com.example.demo.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class FilterDTO {
	
	private String type;
	private LocalDate startdate;
	private LocalDate enddate;
	private String name;
	private String sortField;
	private String sortOrder;

}
