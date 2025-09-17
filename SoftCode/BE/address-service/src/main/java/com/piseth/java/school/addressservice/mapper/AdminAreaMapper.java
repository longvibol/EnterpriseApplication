package com.piseth.java.school.addressservice.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.piseth.java.school.addressservice.domain.AdminArea;
import com.piseth.java.school.addressservice.dto.AdminAreaCreateRequest;
import com.piseth.java.school.addressservice.dto.AdminAreaResponse;
import com.piseth.java.school.addressservice.dto.AdminAreaUpdateRequest;

@Mapper(componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, 
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE // if it null not set mapping 
		)
public interface AdminAreaMapper {
	
	// Map from request to our model in order to insert to database 	
	@Mapping(target = "path", expression = "java(buildPath(dto.getCode()))")
	AdminArea toEntity(AdminAreaCreateRequest dto);
	
	/*
	 
	 12 -> [12]
	 1201 -> [12,1201]
	 120101 -> [12,1201,120101]
	 12010101 -> [12,1201,120101,12010101]- only 8 length of the code 	 
	 
	 */
	default List<String> buildPath(String code){
		
		if(code == null || code.isBlank()) {
			return List.of();
		}
		
		final String trimmed = code.trim();
		final int len = trimmed.length();
		
		if(len%2 !=0 || len >8) {
			//fallback
			return List.of(trimmed);
		}		
		// sub string we take the the hold string only two digit one time 
	
		List<String> path = new ArrayList<>(len /2);
		// (len /2) is the initial value of the array list 
		
		for(int i =2; i <= len;i +=2) {
			path.add(trimmed.substring(0, i));
		}		
		
		return path;
	}
	
	// from Entity to Respond 
	
	AdminAreaResponse toResponse(AdminArea entity);
	
	void update(@MappingTarget AdminArea target, AdminAreaUpdateRequest dto);
}
