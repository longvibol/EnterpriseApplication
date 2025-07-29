package com.piseth.java.school.roomservice.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.piseth.java.school.roomservice.domain.Room;

import reactor.core.publisher.Flux;

@Repository
//Optional @Repository we can remove it in new spring boot 
public interface RoomRepository extends ReactiveMongoRepository<Room, String>{

	//Study Purpose	
	Flux<Room> findByNameContainingIgnoreCase(String name);
		
	//Custom Query 
	@Query("{'name': ?0}")
	Flux<Room> findRoom(String name);
}
