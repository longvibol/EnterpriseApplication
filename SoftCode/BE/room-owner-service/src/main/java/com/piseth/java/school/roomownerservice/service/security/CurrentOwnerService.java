package com.piseth.java.school.roomownerservice.service.security;

import reactor.core.publisher.Mono;

public interface CurrentOwnerService {

	Mono<String> getCurrentOwnerId();
}
