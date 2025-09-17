package com.piseth.java.school.addressservice.service;

import com.piseth.java.school.addressservice.domain.enumeration.AdminLevel;
import com.piseth.java.school.addressservice.dto.AdminAreaCreateRequest;
import com.piseth.java.school.addressservice.dto.AdminAreaResponse;
import com.piseth.java.school.addressservice.dto.AdminAreaUpdateRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AdminAreaService {

	// Create service then it will return object AdminArea (respond) 
	Mono<AdminAreaResponse> create(AdminAreaCreateRequest dto);
	
	//Get
	Mono<AdminAreaResponse> get(String code);
	
	//Delete
	Mono<Void> delete(String code);
	
	//Update 
	
	Mono<AdminAreaResponse> update(String code, AdminAreaUpdateRequest dto);
	
	// list to combo box 
	
	Flux<AdminAreaResponse> list(AdminLevel level, String parentCode);
}
