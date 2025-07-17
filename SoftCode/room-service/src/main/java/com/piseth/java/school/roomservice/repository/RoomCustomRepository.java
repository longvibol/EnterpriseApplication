package com.piseth.java.school.roomservice.repository;

import org.springframework.data.mongodb.core.query.Query;

import com.piseth.java.school.roomservice.domain.Room;

import reactor.core.publisher.Flux;

public interface RoomCustomRepository {
	
	Flux<Room> findByFilter(Query query);

}
