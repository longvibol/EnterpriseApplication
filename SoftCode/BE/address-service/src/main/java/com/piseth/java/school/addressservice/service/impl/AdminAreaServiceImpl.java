package com.piseth.java.school.addressservice.service.impl;

import org.springframework.stereotype.Service;

import com.piseth.java.school.addressservice.domain.AdminArea;
import com.piseth.java.school.addressservice.domain.enumeration.AdminLevel;
import com.piseth.java.school.addressservice.dto.AdminAreaCreateRequest;
import com.piseth.java.school.addressservice.dto.AdminAreaResponse;
import com.piseth.java.school.addressservice.dto.AdminAreaUpdateRequest;
import com.piseth.java.school.addressservice.repository.AdminAreaRepsitory;
import com.piseth.java.school.addressservice.service.AdminAreaService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AdminAreaServiceImpl implements AdminAreaService {
	
	private final AdminAreaRepsitory repsitory;

	@Override
	public Mono<AdminAreaResponse> create(AdminAreaCreateRequest dto) {
		// we create with lazy style when have someone the it will called our validation

		return null;
	}

	// CheckParentCodeExists
	private Mono<Void> CheckParentCodeExists(final AdminArea candidate){
		// check from database 
		if(candidate.getLevel() == AdminLevel.PROVINCE) {
			return Mono.empty();
		}		
		return repsitory.existsById(candidate.getParentCode())
			.flatMap(exists ->{
				if(exists) {
					return Mono.empty();
				}else {
					return Mono.error(new IllegalAccessException("Parent not found : " + candidate.getParentCode()));
				}
			});
		
	}
	
	// EnsureCodeIsUnique	
	private Mono<Void> ensureCodeIsUnique(final AdminArea candidate){
		
		return repsitory.existsById(candidate.getCode())
			.flatMap(exists ->{
				if(exists) {
					return Mono.error(new IllegalAccessException("AdminArea already exists : " + candidate.getCode()));
					
				}else {
					return Mono.empty();
				}
			});
	}
	
	
	
	

	/*
	  Before Save to database 
	  0. map from dto to entity 
	  1. Basic Validate 
	  2. CheckParentCodeExists (same time) 
	  2. ensureCodeIsUnique (same time) 
	  3. save 
	  4. map to response
	*/

	@Override
	public Mono<AdminAreaResponse> get(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Void> delete(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<AdminAreaResponse> update(AdminAreaUpdateRequest dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Flux<AdminAreaResponse> list(AdminLevel level, String parentCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
