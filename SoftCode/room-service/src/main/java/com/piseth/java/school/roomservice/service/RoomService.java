package com.piseth.java.school.roomservice.service;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.dto.RoomDTO;
import com.piseth.java.school.roomservice.dto.RoomFilterDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomService {
	
	Mono<RoomDTO> createRoom(RoomDTO roomDTO);
	Mono<RoomDTO> getRoomById(String id);
	Flux<Room> getAllRoom();
	Mono<RoomDTO> updateRoom(String id, RoomDTO roomDTO);	
	Mono<Void> deleteRoom(String id);
	
	//Study purpose only	
	Flux<RoomDTO> searchRoomByName(String name);

	//service with our query criteria 	
	Flux<RoomDTO> getRoomByFilter(RoomFilterDTO filterDTO);
}
