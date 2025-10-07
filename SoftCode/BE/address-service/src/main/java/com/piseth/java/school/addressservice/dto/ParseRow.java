package com.piseth.java.school.addressservice.dto;

import com.piseth.java.school.addressservice.domain.enumeration.AdminLevel;

public record ParseRow(
		int lineNumber,
		String code,
		AdminLevel level,
		String parentCode,
		String nameKh,
		String nameEn		
) {

}
