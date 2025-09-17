package com.piseth.java.school.addressservice.validator;

import java.util.Objects;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.piseth.java.school.addressservice.domain.AdminArea;
import com.piseth.java.school.addressservice.domain.enumeration.AdminLevel;
import com.piseth.java.school.addressservice.exception.ValidationException;

@Component
public class AdminAreaValidator {
	
	private static final Pattern CODE_PATTERN = Pattern.compile("^\\d{2}(?:\\d{2}){0,3}$");
	
	public void validate(AdminArea request) {
		
		if(Objects.isNull(request)) {
			throw new ValidationException("request is required");
		}
		
		if(Objects.isNull(request.getLevel())) {
			throw new ValidationException("level is required");
		}
		
		if(Objects.isNull(request.getCode()) || request.getCode().isBlank()) {
			throw new ValidationException("code is required");
		}
		
		
		if(!CODE_PATTERN.matcher(request.getCode()).matches()) {
			throw new ValidationException("code must look like 12 or 12030902");
		}
		
		// code : level of the location : province, district etc  
		final String code = request.getCode();
		final int depth = code.length() /2; // it will return 2, 
		
		final int expectedDepth = request.getLevel().depth();
		
		if(depth != expectedDepth) {
			throw new ValidationException("Code depth does not match level : " + request.getLevel());
		}
		
		if(request.getLevel() == AdminLevel.PROVINCE) {
			if(request.getParentCode() != null) {
				throw new ValidationException("ParentCode must be null for PROVINCE");
			}
		}else {
			String parentCode = request.getParentCode();
			
			if(Objects.isNull(parentCode) || parentCode.isBlank()) {
				throw new IllegalArgumentException("ParentCode is required for : " + request.getLevel());
			}
			
			// validate parent code with the pattern 
			
			if(!CODE_PATTERN.matcher(parentCode).matches()) {
				throw new ValidationException("ParentCode must look like 12 or 120309");
			}
			
			if(!request.getCode().startsWith(parentCode)) {
				throw new IllegalArgumentException("code must start with parentCode");
			}
		}
		
		
		
	}

}