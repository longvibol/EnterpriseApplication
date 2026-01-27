package com.piseth.java.school.roomservice.service;

import java.util.List;

import com.piseth.java.school.roomservice.dto.NearbyRoomFilterDTO;
import com.piseth.java.school.roomservice.dto.PageDTO;
import com.piseth.java.school.roomservice.dto.RoomDTO;
import com.piseth.java.school.roomservice.dto.RoomFilterDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomService {
	
	Mono<RoomDTO> getRoomById(String id);
	
	Mono<PageDTO<RoomDTO>> getRoomByFilterPagination(RoomFilterDTO filterDTO);
	Flux<RoomDTO> getRoomsByIds(List<String> ids);
	
	Mono<PageDTO<RoomDTO>> getNearestRooms(NearbyRoomFilterDTO filter);
	
}