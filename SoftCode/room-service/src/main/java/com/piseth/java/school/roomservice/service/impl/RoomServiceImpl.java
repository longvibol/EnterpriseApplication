package com.piseth.java.school.roomservice.service.impl;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.dto.PageDTO;
import com.piseth.java.school.roomservice.dto.RoomDTO;
import com.piseth.java.school.roomservice.dto.RoomFilterDTO;
import com.piseth.java.school.roomservice.exception.RoomNotFoundException;
import com.piseth.java.school.roomservice.mapper.RoomMapper;
import com.piseth.java.school.roomservice.repository.RoomCustomRepository;
import com.piseth.java.school.roomservice.repository.RoomRepository;
import com.piseth.java.school.roomservice.service.RoomService;
import com.piseth.java.school.roomservice.util.RoomCriteriaBuilder;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class RoomServiceImpl implements RoomService {
	
	private final RoomRepository roomRepository; 

	private final RoomMapper roomMapper;
	
	private final RoomCustomRepository roomCustomRepository;

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
		log.info("Retreiving room with ID : {}",id);		
		return roomRepository
				.findById(id)
				// we want to show logger the we can find the room 
				.switchIfEmpty(Mono.error(new RoomNotFoundException(id)))
				.doOnNext(room -> log.info("Room received : {}",room))
				.map(roomMapper::toRoomDTO);				
	}
	
	@Override
	public Flux<Room> getAllRoom() {
        return roomRepository.findAll()
            .doOnSubscribe(subscription -> log.info("Fetching all rooms"))
            .doOnNext(room -> log.debug("Found room: {}", room))
            .doOnComplete(() -> log.info("Completed fetching rooms"))
            .doOnError(error -> log.error("Error fetching rooms", error))
            .switchIfEmpty(Flux.error(new RoomNotFoundException("No rooms found")));
    }

	
	@Override
	public Mono<RoomDTO> updateRoom(String id, RoomDTO roomDTO) {		
	log.debug("Update room id: {} with data : {}",id,roomDTO);			
	return	roomRepository				
				.findById(id)
				.switchIfEmpty(Mono.error(new RoomNotFoundException(id)))
				.flatMap(existing ->{							
					roomMapper.updateRoomFromDTO(roomDTO, existing);					
					Mono<Room> monoRoom = roomRepository.save(existing);						
					return monoRoom;
				}).map(roomMapper::toRoomDTO);		
	}

	@Override
	public Mono<Void> deleteRoom(String id) {
		log.info("Deleting room with ID: {}",id);
		return roomRepository.deleteById(id)
				.switchIfEmpty(Mono.error(new RoomNotFoundException(id)))
				.doOnSubscribe(deleted -> log.info("\"Room deleted with ID: {}",id));

	}

	@Override
	public Flux<RoomDTO> searchRoomByName(String name) {		
		
		return roomRepository.findRoom(name).map(roomMapper::toRoomDTO);
		
//		return roomRepository.findByNameContainingIgnoreCase(name)
//				.map(roomMapper::toRoomDTO);
	}

	@Override
	public Flux<RoomDTO> getRoomByFilter(RoomFilterDTO filterDTO) {
		Criteria criteria = RoomCriteriaBuilder.build(filterDTO);		
		return roomCustomRepository.findByFilter(new Query(criteria))
				.map(roomMapper::toRoomDTO);
	}

	@Override
	public Mono<PageDTO<RoomDTO>> getRoomByFilterPagination(RoomFilterDTO filterDTO) {
		Criteria criteria = RoomCriteriaBuilder.build(filterDTO);		
		
		Mono<Long> countMono = roomCustomRepository.coundByFilter(new Query(criteria));

		Query query = new Query(criteria)
				.skip((long) filterDTO.getPage() * filterDTO.getSize())
				.limit(filterDTO.getSize());
		
		query.with(RoomCriteriaBuilder.sort(filterDTO));
		
		Flux<RoomDTO> contentFlux = roomCustomRepository.findByFilter(query).map(roomMapper::toRoomDTO);
		
		return Mono.zip(countMono, contentFlux.collectList())
				.map(tuple ->{
					long total = tuple.getT1();
					List<RoomDTO> content = tuple.getT2(); 
					int totalPages = (int) Math.ceil((double)total/ filterDTO.getSize());
					return new PageDTO<>(filterDTO.getPage(), filterDTO.getSize(),total,totalPages, content);
				});
	}		
	
}


