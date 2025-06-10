package com.piseth.java.school.roomservice.service;

import com.piseth.java.school.roomservice.dto.RoomDTO;

import reactor.core.publisher.Mono;

public interface RoomService {
	
	Mono<RoomDTO> createRoom(RoomDTO roomDTO);
	Mono<RoomDTO> getRoomById(String id);
	Mono<RoomDTO> updateRoom(String id, RoomDTO roomDTO);

}
