package com.piseth.java.school.addressservice.dto;

import java.util.Comparator;
import java.util.regex.Pattern;

import com.piseth.java.school.addressservice.domain.enumeration.AdminLevel;

public record ParseRow(
		int lineNumber,
		String code,
		AdminLevel level,
		String parentCode,
		String nameKh,
		String nameEn		
){
	private static final Pattern CODE_PATTERN = Pattern.compile("^\\d{2}(?:\\d{2}){0,3}$");
	// 3- create fn trim code ( 8 digit ) - String code	
	private String trimmedCode() {
		if(code == null) {
			return "";
		}
		return code.trim();
	}
	
	// 2-sort by the code level depth 1=provicen ; 2= disctirce ; 3 =communte 
	public int depth() {
		final String c = trimmedCode();
		if ( c.isEmpty() || !CODE_PATTERN.matcher(c).matches()) {
			return Integer.MAX_VALUE;
		}
		return c.length() / 2;
	}
	
	// 1-create comparator fu 
	
	public static final Comparator<ParseRow> BY_DEPTH = 
			Comparator.comparingInt(ParseRow::depth)
				.thenComparing(ParseRow::trimmedCode)
				.thenComparing(ParseRow::lineNumber);

}
