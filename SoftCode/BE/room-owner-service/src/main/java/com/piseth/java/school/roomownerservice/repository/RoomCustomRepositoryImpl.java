package com.piseth.java.school.roomownerservice.repository;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.piseth.java.school.roomownerservice.domain.Room;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class RoomCustomRepositoryImpl implements RoomCustomRepository{
	private final ReactiveMongoTemplate mongoTemplate;

	@Override
	public Flux<Room> findByFilter(Query query) {
		return mongoTemplate.find(query, Room.class);
	}

	@Override
	public Mono<Long> countByFilter(Query query) {
		return mongoTemplate.count(query, Room.class);
	}
	

}