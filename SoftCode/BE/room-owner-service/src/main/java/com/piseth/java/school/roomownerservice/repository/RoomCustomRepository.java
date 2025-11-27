package com.piseth.java.school.roomownerservice.repository;

import org.springframework.data.mongodb.core.query.Query;

import com.piseth.java.school.roomownerservice.domain.Room;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomCustomRepository {
	
	Flux<Room> findByFilter(Query query);
	
	Mono<Long> countByFilter(Query query);

}