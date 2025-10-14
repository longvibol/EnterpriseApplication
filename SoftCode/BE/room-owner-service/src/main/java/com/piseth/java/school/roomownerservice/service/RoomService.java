package com.piseth.java.school.roomownerservice.service;

import com.piseth.java.school.roomownerservice.dto.RoomCreateRequest;
import com.piseth.java.school.roomownerservice.dto.RoomResponse;
import com.piseth.java.school.roomownerservice.dto.RoomUpdateRequest;

import reactor.core.publisher.Mono;

public interface RoomService {
	/*
	Mono<RoomDTO> createRoom(RoomDTO roomDTO);
	Mono<RoomDTO> getRoomById(String id);
	Flux<Room> getAllRoom();
	Mono<RoomDTO> updateRoom(String id, RoomDTO roomDTO);	
	Mono<Void> deleteRoom(String id);
	
	//Study purpose only	
	Flux<RoomDTO> searchRoomByName(String name);

	//service with our query criteria 	
	Flux<RoomDTO> getRoomByFilter(RoomFilterDTO filterDTO);
	
	//Filter by pagination 	
	Mono<PageDTO<RoomDTO>> getRoomByFilterPagination(RoomFilterDTO filterDTO);
	
	Mono<RoomImportSummary> updateRoomSuccess();
	Mono<RoomImportSummary> updateRoomFail();
	*/
	
	Mono<RoomResponse> create(RoomCreateRequest request);
	Mono<RoomResponse> update(String id, RoomUpdateRequest request);
	Mono<Void> delete(String id);
	
	
	
}
