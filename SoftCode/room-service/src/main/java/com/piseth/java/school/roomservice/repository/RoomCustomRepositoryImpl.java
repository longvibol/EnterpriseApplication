package com.piseth.java.school.roomservice.repository;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.piseth.java.school.roomservice.domain.Room;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class RoomCustomRepositoryImpl implements RoomCustomRepository{
	
	private final ReactiveMongoTemplate mongoTemplate;
	// in order to create dynamic we use ReactiveMongoTemplate 

	@Override
	public Flux<Room> findByFilter(Query query) {
		return mongoTemplate.find(query, Room.class);
	}
}
