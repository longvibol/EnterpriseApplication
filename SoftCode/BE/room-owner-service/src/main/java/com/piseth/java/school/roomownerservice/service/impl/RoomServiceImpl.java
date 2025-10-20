package com.piseth.java.school.roomownerservice.service.impl;


import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piseth.java.school.roomownerservice.config.MessagingProperties;
import com.piseth.java.school.roomownerservice.config.OutboxProperties;
import com.piseth.java.school.roomownerservice.domain.Room;
import com.piseth.java.school.roomownerservice.domain.enumeration.RoomStatus;
import com.piseth.java.school.roomownerservice.dto.RoomCreateRequest;
import com.piseth.java.school.roomownerservice.dto.RoomResponse;
import com.piseth.java.school.roomownerservice.dto.RoomUpdateRequest;
import com.piseth.java.school.roomownerservice.mapper.RoomMapper;
import com.piseth.java.school.roomownerservice.messaging.event.RoomEventEnvelope;
import com.piseth.java.school.roomownerservice.messaging.event.RoomEventType;
import com.piseth.java.school.roomownerservice.messaging.event.RoomFullPayload;
import com.piseth.java.school.roomownerservice.outbox.OutboxEvent;
import com.piseth.java.school.roomownerservice.outbox.OutboxRepository;
import com.piseth.java.school.roomownerservice.outbox.OutboxStatus;
import com.piseth.java.school.roomownerservice.repository.RoomRepository;
import com.piseth.java.school.roomownerservice.service.RoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class RoomServiceImpl implements RoomService{
	
	private final RoomRepository repository;
	private final OutboxRepository outboxRepo;
	private final RoomMapper mapper;
	private final MessagingProperties msgProps;
	private final OutboxProperties outboxProps;
	private final ObjectMapper objectMapper;
	
	//@Transactional // protech when outbox have problem 
	@Override
	public Mono<RoomResponse> create(RoomCreateRequest request) {
		// save to db : room collection
		// outbox : outbox collection
		// x filter from outbox to send kafka message
		
		
		final Room entity = mapper.toEntity(request);

	      if (entity.getStatus() == null) {
	          entity.setStatus(RoomStatus.AVAILABLE);
	      }

	      return repository.insert(entity) // save 
	              .flatMap(saved -> enqueueEvent(saved, RoomEventType.ROOM_CREATED, "create").thenReturn(saved))
	              .map(mapper::toResponse)
	              .doOnSuccess(r -> log.info("Room created id={}, ownerId={}", r.getId(), r.getOwnerId()))
	              .doOnError(ex -> log.error("Create room failed: {}", ex.getMessage(), ex));
	}

	@Override
	public Mono<RoomResponse> update(String id, RoomUpdateRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Void> delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Mono<OutboxEvent> enqueueEvent(final Room room,
            final RoomEventType type,
            final String actionHeader) {
		final RoomFullPayload payload = mapper.toFullPayload(room);
		
		final RoomEventEnvelope<RoomFullPayload> envelope =
			RoomEventEnvelope.of(
			type,
			msgProps.getEventVersion(),
			msgProps.getProducerId(),
			room.getId(),
			payload,
			Map.of("action", actionHeader));
		
		final String json;
		try {
		json = objectMapper.writeValueAsString(envelope);
		} catch (Exception e) {
		return Mono.error(new IllegalStateException("Failed to serialize event payload.", e));
		}
		
		final OutboxEvent evt = OutboxEvent.builder()
				.aggregateId(room.getId())
				.aggregateType("Room")
				.eventType(type.name())
				.eventVersion(msgProps.getEventVersion())
				.producer(msgProps.getProducerId())
				.key(room.getId())
				.topic(msgProps.getTopics().getRoomEvents())
				.headers(Map.of())
				.payloadJson(json)
				.occurredAt(LocalDateTime.now())
				.status(OutboxStatus.PENDING)
				.attempt(0)
				.availableAt(LocalDateTime.now())
				.build();
		
		return outboxRepo.save(evt);
}
	
	
	
	/*
	
	private final RoomRepository roomRepository;
	private final RoomMapper roomMapper;
	private final RoomCustomRepository roomCustomRepository;
	
	@Override
	public Mono<RoomDTO> createRoom(RoomDTO roomDTO) {
		log.info("Saving room to DB: {}", roomDTO);
		
		Room room = roomMapper.toRoom(roomDTO);
		
		return roomRepository.save(room)
			.doOnSuccess(saved -> log.info("Room saved: {}",saved))
			.map(roomMapper::toRoomDTO);
		 
	}

	@Override
	public Mono<RoomDTO> getRoomById(String id) {
		log.info("Retreiving room with ID: {}", id);
		return roomRepository.findById(id)
				.switchIfEmpty(Mono.error(new RoomNotFoundException(id)))
				.doOnNext(room -> log.info("Room received : {}", room))
				.map(roomMapper::toRoomDTO);
				
	}

	@Override
	public Mono<RoomDTO> updateRoom(String id, RoomDTO roomDTO) {
		log.debug("Updating romm id: {} with data : {}", id, roomDTO);
		
		  return roomRepository.findById(id)
				  	.switchIfEmpty(Mono.error(new RoomNotFoundException(id)))
					.flatMap(existing ->{
						roomMapper.updateRoomFromDTO(roomDTO, existing);
						return roomRepository.save(existing);
					})
					.map(roomMapper::toRoomDTO);
		
	}

	@Override
	public Mono<Void> deleteRoom(String id) {
		log.info("Deleting room with ID: {}",id);
		return roomRepository.deleteById(id)
				.switchIfEmpty(Mono.error(new RoomNotFoundException(id)))
				.doOnSuccess(deleted -> log.info("Room deleted with ID: {}",id));
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

	
*/
}