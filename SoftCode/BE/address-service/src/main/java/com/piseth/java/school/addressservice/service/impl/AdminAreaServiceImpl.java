package com.piseth.java.school.addressservice.service.impl;

import org.springframework.stereotype.Service;

import com.piseth.java.school.addressservice.domain.AdminArea;
import com.piseth.java.school.addressservice.domain.enumeration.AdminLevel;
import com.piseth.java.school.addressservice.dto.AdminAreaCreateRequest;
import com.piseth.java.school.addressservice.dto.AdminAreaResponse;
import com.piseth.java.school.addressservice.dto.AdminAreaUpdateRequest;
import com.piseth.java.school.addressservice.mapper.AdminAreaMapper;
import com.piseth.java.school.addressservice.repository.AdminAreaRepsitory;
import com.piseth.java.school.addressservice.service.AdminAreaService;
import com.piseth.java.school.addressservice.validator.AdminAreaValidator;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AdminAreaServiceImpl implements AdminAreaService {
	
	private final AdminAreaRepsitory repository;
	private final AdminAreaValidator validator;
	private final AdminAreaMapper mapper;

	@Override
	public Mono<AdminAreaResponse> create(AdminAreaCreateRequest dto) {
		// we create with lazy style when have someone the it will called our validation
		return Mono.fromCallable(()->{
			final AdminArea candidate = mapper.toEntity(dto);			
			validator.validate(candidate);
			return candidate;
		}).flatMap(cadidate->{
//			CheckParentCodeExists(candidate)
//			.then(ensureCodeIsUnique(candidate)); because we can do it at the same time
			
			return Mono.when(CheckParentCodeExists(cadidate),ensureCodeIsUnique(cadidate))
			.thenReturn(cadidate)
			// to save 
			.flatMap(c -> repository.save(c))
			.map(mapper::toResponse);
		});
	}

	// CheckParentCodeExists
	private Mono<Void> CheckParentCodeExists(final AdminArea candidate){
		// check from database 
		if(candidate.getLevel() == AdminLevel.PROVINCE) {
			return Mono.empty();
		}		
		return repository.existsById(candidate.getParentCode())
			.flatMap(exists ->{
				if(exists) {
					return Mono.empty();
				}else {
					return Mono.error(new IllegalAccessException("Parent not found : " + candidate.getParentCode()));
				}
			});
		
	}
	
	// EnsureCodeIsUnique ; check duplicate
	private Mono<Void> ensureCodeIsUnique(final AdminArea candidate){
		
		return repository.existsById(candidate.getCode())
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
