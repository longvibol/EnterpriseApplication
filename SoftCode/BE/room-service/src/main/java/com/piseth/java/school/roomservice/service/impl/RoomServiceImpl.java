package com.piseth.java.school.roomservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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
public class RoomServiceImpl implements RoomService{
	
	private final RoomRepository roomRepository;
	private final RoomMapper roomMapper;
	private final RoomCustomRepository roomCustomRepository;
	
	@Value("${storage.publicBaseUrl}")
	private String publicBaseUrl;

	

	@Override
	public Mono<RoomDTO> getRoomById(String id) {
		log.info("Retreiving room with ID: {}", id);
		return roomRepository.findById(id)
				.switchIfEmpty(Mono.error(new RoomNotFoundException(id)))
				.doOnNext(room -> log.info("Room received : {}", room))
				//.map(roomMapper::toRoomDTO);
				.flatMap(this::toResponseWithUrls);
				
	}

	@Override
	public Mono<PageDTO<RoomDTO>> getRoomByFilterPagination(RoomFilterDTO filterDTO) {
		Criteria criteria = RoomCriteriaBuilder.build(filterDTO);
		
		Mono<Long> countMono = roomCustomRepository.coundByFilter(new Query(criteria));
		
		Query query = new Query(criteria)
				.skip((long) filterDTO.getPage() * filterDTO.getSize())
				.limit(filterDTO.getSize());
		
		query.with(RoomCriteriaBuilder.sort(filterDTO));
		
		Flux<RoomDTO> contentFlux = roomCustomRepository.findByFilter(query)
										.map(roomMapper::toRoomDTO);
		
		return Mono.zip(countMono, contentFlux.collectList())
			.map(tuple ->{
				long total = tuple.getT1();
				List<RoomDTO> content = tuple.getT2(); 
				int totalPages = (int) Math.ceil((double)total/ filterDTO.getSize());
				return new PageDTO<>(filterDTO.getPage(), filterDTO.getSize(),total,totalPages, content);
			});
		
	}
	
	@Override
	public Flux<RoomDTO> getRoomsByIds(List<String> ids) {
	    if (ids == null || ids.isEmpty()) {
	        return Flux.empty();
	    }

	    return roomRepository.findAllById(ids)
	            .map(roomMapper::toRoomDTO);
	}
	
	private Mono<RoomDTO> toResponseWithUrls(Room room) {
		RoomDTO resp = roomMapper.toRoomDTO(room);
	    List<String> keys = room.getPhotoObjectKeys() == null ? List.of() : room.getPhotoObjectKeys();

	    return Flux.fromIterable(keys)
	        .map(key -> publicBaseUrl + "/room-media/" + key) // if bucket is public
	        // or presignedGetUrl if visitor service has MinIO creds
	        .collectList()
	        .map(urls -> {
	            resp.setPhotoUrls(urls);
	            return resp;
	        });
	}

	

}