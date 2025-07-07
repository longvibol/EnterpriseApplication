package com.piseth.java.school.roomservice.service.impl;

import org.springframework.stereotype.Service;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.dto.RoomDTO;
import com.piseth.java.school.roomservice.mapper.RoomMapper;
import com.piseth.java.school.roomservice.repository.RoomRepository;
import com.piseth.java.school.roomservice.service.RoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class RoomServiceImpl implements RoomService {
	
	private final RoomRepository roomRepository;

	private final RoomMapper roomMapper;

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
				.findById(id).flatMap(existing ->{							
					roomMapper.updateRoomFromDTO(roomDTO, existing);					
					Mono<Room> monoRoom = roomRepository.save(existing);						
					return monoRoom;
				}).map(roomMapper::toRoomDTO);		
	}

	@Override
	public Mono<Void> deleteRoom(String id) {
		log.info("Deleting room with ID: {}",id);
		return roomRepository.deleteById(id)
				.doOnSubscribe(deleted -> log.info("\"Room deleted with ID: {}",id));

	}
	
}


