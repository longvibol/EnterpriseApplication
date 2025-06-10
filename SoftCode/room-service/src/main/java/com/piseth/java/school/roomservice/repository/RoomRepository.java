package com.piseth.java.school.roomservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.piseth.java.school.roomservice.domain.Room;

@Repository
//Optional @Repository we can remove it in new spring boot 
public interface RoomRepository extends ReactiveMongoRepository<Room, String>{

}
