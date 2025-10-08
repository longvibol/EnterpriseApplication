package com.piseth.java.school.addressservice.dto;

import lombok.Data;

@Data
public class RowError {
	
	private int line;
	private String code;
	private String message;

}
