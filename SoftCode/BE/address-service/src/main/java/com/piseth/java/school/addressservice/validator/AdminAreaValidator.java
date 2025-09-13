package com.piseth.java.school.addressservice.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.piseth.java.school.addressservice.domain.AdminArea;
import com.piseth.java.school.addressservice.domain.enumeration.AdminLevel;

@Component
public class AdminAreaValidator {
	
	private static final String CODE_PATTERN = "\"^[0-9]{2}(?:-[0-9]{2}){0,3}$\"";
	
	// validate Entity level and no database access 
	// validate before send to database to create 
	public void validator(AdminArea request) {
		
		if(Objects.isNull(request)) {
			throw new IllegalArgumentException("request is required");
		}
		
		if(Objects.isNull(request.getLevel())) {
			throw new IllegalArgumentException("level is required");
		}
		
		if(Objects.isNull(request.getCode()) || request.getCode().isBlank()) {
			throw new IllegalArgumentException("code is required");
		}
		
		if(!CODE_PATTERN.matches(request.getCode())) {
			throw new IllegalArgumentException("code must be look like 12 or 12-03--09-02");
		}
		
		// validate to the deep 
		
		final int depth = request.getCode().split("-").length;
		final int expectedDepth = request.getLevel().depth();
		
		if(depth !=expectedDepth) {
			throw new IllegalArgumentException("code depth does not match level : " + request.getLevel());
		}
		
		// They don't throw parent code 
		
		if(request.getLevel() == AdminLevel.PROVINCE) {
			if(request.getParentCode() !=null) {
				throw new IllegalArgumentException("ParentCode must be null for PROVINCE");
			}
		}else {
			if(Objects.isNull(request.getParentCode())|| request.getParentCode().isBlank()) {
				throw new IllegalArgumentException("ParentCode is required for : " + request.getLevel());
			}
			
			if(request.getCode().startsWith(request.getParentCode())) {
				
				// 12-03 but they input 14 different from 12 
				throw new IllegalArgumentException("Code must start with parentCode");
			}
		}	
		
		
	}

}
