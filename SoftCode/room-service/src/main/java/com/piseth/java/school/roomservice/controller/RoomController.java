package com.piseth.java.school.roomservice.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piseth.java.school.roomservice.dto.RoomDTO;
import com.piseth.java.school.roomservice.service.RoomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/api/rooms")
public class RoomController {
		
	private final RoomService roomService;
		
	@PostMapping
	public Mono<RoomDTO> createRoom(@Valid @RequestBody RoomDTO roomDTO){
		return roomService.createRoom(roomDTO);		
	}

	@GetMapping("/{roomId}")
	public Mono<RoomDTO> getRoomById(@PathVariable String roomId){
		return roomService.getRoomById(roomId);
	}
	
	@PutMapping("/{roomId}")
	public Mono<RoomDTO> updateRoom(@PathVariable String roomId,@RequestBody RoomDTO roomDTO){
		return roomService.updateRoom(roomId, roomDTO);
	}
	
	@DeleteMapping("/{roomId}")
	public Mono<Void> deleteRoom(@PathVariable String roomId){
		return roomService.deleteRoom(roomId);
	}
}





















