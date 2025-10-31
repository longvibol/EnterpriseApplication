package com.piseth.java.school.roomservice.service.impl;


import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.piseth.java.school.roomservice.domain.ProcessedEvent;
import com.piseth.java.school.roomservice.mapper.RoomProjectionMapper;
import com.piseth.java.school.roomservice.message.event.RoomEventEnvelope;
import com.piseth.java.school.roomservice.message.event.RoomEventType;
import com.piseth.java.school.roomservice.message.event.RoomFullPayload;
import com.piseth.java.school.roomservice.repository.ProcessedEventRepository;
import com.piseth.java.school.roomservice.repository.RoomRepository;
import com.piseth.java.school.roomservice.service.RoomProjectionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomProjectionServiceImpl implements RoomProjectionService {

    private final RoomRepository roomRepo;
    private final ProcessedEventRepository eventRepo;
    private final RoomProjectionMapper mapper;

    @Override
    public Mono<Void> apply(final RoomEventEnvelope<RoomFullPayload> env) {
        final String roomId = env.getAggregateId();

        return eventRepo.findByAggregateTypeAndAggregateId("Room", roomId)
            .defaultIfEmpty(ProcessedEvent.builder()
                    .aggregateType("Room")
                    .aggregateId(roomId)
                    .lastEventId(null)
                    .lastOccurredAt(null)
                    .build())
            .flatMap(pe -> {
            	// pe = process event 
            	// idempotent : No duplication 
                if (pe.getLastOccurredAt() != null && pe.getLastOccurredAt().compareTo(env.getOccurredAt()) >= 0) {
                    log.debug("Skip stale/duplicate event {} for room {}", env.getEventId(), roomId);
                    return Mono.empty();
                }

                if (env.getType() == RoomEventType.ROOM_DELETED) {
                    return softDelete(roomId, env, pe);
                } else {
                    return upsertProjection(env.getData(), env, pe);
                }
            })
            .then();
    }

    private Mono<Void> upsertProjection(final RoomFullPayload payload,
                                        final RoomEventEnvelope<RoomFullPayload> env,
                                        final ProcessedEvent pe) {
        return roomRepo.findById(payload.getId())
            .defaultIfEmpty(mapper.toProjection(payload))
            .flatMap(existing -> {
                mapper.merge(existing, payload);
                existing.setId(payload.getId());
                existing.setLastEventAt(LocalDateTime.ofInstant(env.getOccurredAt(), java.time.ZoneOffset.UTC));
                existing.setDeleted(false);
                return roomRepo.save(existing);
            })
            .then(updateProcessed(env, pe));
    }

    private Mono<Void> softDelete(final String id,
                                  final RoomEventEnvelope<RoomFullPayload> env,
                                  final ProcessedEvent pe) {
        return roomRepo.findById(id)
            .flatMap(doc -> {
                doc.setDeleted(true);
                doc.setLastEventAt(LocalDateTime.ofInstant(env.getOccurredAt(), java.time.ZoneOffset.UTC));
                return roomRepo.save(doc);
            })
            .switchIfEmpty(Mono.empty())
            .then(updateProcessed(env, pe));
    }

    private Mono<Void> updateProcessed(final RoomEventEnvelope<RoomFullPayload> env,
                                       final ProcessedEvent pe) {
        pe.setLastEventId(env.getEventId());
        pe.setLastOccurredAt(env.getOccurredAt());
        return eventRepo.save(pe).then();
    }
}