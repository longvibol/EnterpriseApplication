package com.piseth.java.school.roomservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.dto.RoomDTO;
import com.piseth.java.school.roomservice.mapper.RoomMapper;
import com.piseth.java.school.roomservice.repository.RoomRepository;
import com.piseth.java.school.roomservice.service.RoomService;

import reactor.core.publisher.Mono;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	RoomMapper roomMapper;

	@Override
	public Mono<RoomDTO> createRoom(RoomDTO roomDTO) {
		Room room = roomMapper.toRoom(roomDTO);
		return roomRepository.save(room)
				.map(r -> roomMapper.toRoomDTO(r));

	}

}
