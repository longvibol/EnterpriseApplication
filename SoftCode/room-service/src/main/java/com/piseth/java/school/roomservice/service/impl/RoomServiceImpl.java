package com.piseth.java.school.roomservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.dto.RoomDTO;
import com.piseth.java.school.roomservice.mapper.RoomMapper;
import com.piseth.java.school.roomservice.repository.RoomRepository;
import com.piseth.java.school.roomservice.service.RoomService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService {
	
	@Autowired
	RoomRepository roomRepository;

	@Autowired
	RoomMapper roomMapper;

	@Override
	public Mono<RoomDTO> createRoom(RoomDTO roomDTO) {
		
		log.debug("Saving room to DB: {}",roomDTO);
		
		Room room = roomMapper.toRoom(roomDTO);
		
		return roomRepository.save(room)
				.doOnSuccess(saved -> log.info("Room Save: {}",saved))
				.map(roomMapper::toRoomDTO);

	}

	@Override
	public Mono<RoomDTO> getRoomById(String id) {
		log.debug("Retreiving room with ID : {}",id);
		
		return roomRepository
				.findById(id)
				// we want to show logger the we can find the room 
				.doOnNext(room -> log.info("Room received : {}",room))
				.map(roomMapper::toRoomDTO);
				
	}

	@Override
	public Mono<RoomDTO> updateRoom(String id, RoomDTO roomDTO) {
		log.debug("Update room id: {} with data : {}",id,roomDTO);		
		
	return	roomRepository
				.findById(id).flatMap(existingRoom ->{
					existingRoom.setName(roomDTO.getName());
					existingRoom.setAttributes(roomDTO.getAttributes());
					Mono<Room> monoRoom = roomRepository.save(existingRoom);	
					return monoRoom;
				})
				.map(roomMapper::toRoomDTO);
		
		// Target: update existing object		
		// 1-get from DB
		// 2-update new value 
		// 3-save back to DB		

	}

	
}


