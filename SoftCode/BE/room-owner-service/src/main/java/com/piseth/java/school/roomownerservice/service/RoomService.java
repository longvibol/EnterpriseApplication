package com.piseth.java.school.roomownerservice.service;

import com.piseth.java.school.roomownerservice.dto.PageDTO;
import com.piseth.java.school.roomownerservice.dto.RoomCreateRequest;
import com.piseth.java.school.roomownerservice.dto.RoomFilterDTO;
import com.piseth.java.school.roomownerservice.dto.RoomResponse;
import com.piseth.java.school.roomownerservice.dto.RoomUpdateRequest;

import reactor.core.publisher.Mono;

public interface RoomService {
	
	Mono<RoomResponse> create(RoomCreateRequest request, String ownerId);
	Mono<RoomResponse> update(String id, RoomUpdateRequest request, String ownerId);
	Mono<Void> delete(String id, String ownerId);
	Mono<RoomResponse> getById(String id, String ownerId);
	Mono<PageDTO<RoomResponse>> getRoomByFilterPagination(RoomFilterDTO filterDTO, String ownerId);
	
	
}