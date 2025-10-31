package com.piseth.java.school.roomservice.service;

import com.piseth.java.school.roomservice.message.event.RoomEventEnvelope;
import com.piseth.java.school.roomservice.message.event.RoomFullPayload;

import reactor.core.publisher.Mono;

public interface RoomProjectionService {
    Mono<Void> apply(RoomEventEnvelope<RoomFullPayload> envelope);
}