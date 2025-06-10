package com.piseth.java.school.roomservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piseth.java.school.roomservice.dto.RoomDTO;
import com.piseth.java.school.roomservice.service.impl.RoomServiceImpl;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/rooms")
public class RoomController {
	
	@Autowired
	RoomServiceImpl roomServiceImpl;
	
	
	@PostMapping
	public Mono<RoomDTO> createRoom(@RequestBody RoomDTO roomDTO){
		return roomServiceImpl.createRoom(roomDTO);		
	}

}
