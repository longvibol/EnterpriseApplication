package com.piseth.java.school.roomservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.piseth.java.school.roomservice.dto.PageDTO;
import com.piseth.java.school.roomservice.dto.RoomDTO;
import com.piseth.java.school.roomservice.dto.RoomFilterDTO;
import com.piseth.java.school.roomservice.service.RoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rooms")
public class RoomController {
	
	private final RoomService roomService;	
	
	
	@GetMapping("/{roomId}")
	@Operation(summary = "Get room by ID", parameters = @Parameter(in = ParameterIn.PATH, name = "roomId"))
	public Mono<RoomDTO> getRoomById(@PathVariable String roomId){
		return roomService.getRoomById(roomId);
	}
		
	@GetMapping("/search/pagination")
	public Mono<PageDTO<RoomDTO>> getRoomByFilterPagination(RoomFilterDTO roomFilterDTO){
		return roomService.getRoomByFilterPagination(roomFilterDTO);
	}
	
	@GetMapping("/by-ids")
	@Operation(summary = "Get rooms by IDs (for favorites)")
	public Flux<RoomDTO> getRoomsByIds(@RequestParam List<String> ids) {
	    return roomService.getRoomsByIds(ids);
	}
	

}