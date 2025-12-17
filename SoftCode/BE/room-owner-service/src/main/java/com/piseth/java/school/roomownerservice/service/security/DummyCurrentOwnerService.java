package com.piseth.java.school.roomownerservice.service.security;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class DummyCurrentOwnerService implements CurrentOwnerService{

	@Override
	public Mono<String> getCurrentOwnerId() {
		// For now: fixed ownwer id, later we get from request header
		return Mono.just("123");
	}

}
