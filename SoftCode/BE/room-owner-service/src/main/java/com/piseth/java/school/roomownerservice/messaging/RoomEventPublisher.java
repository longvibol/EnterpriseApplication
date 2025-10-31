package com.piseth.java.school.roomownerservice.messaging;

import com.piseth.java.school.roomownerservice.messaging.event.RoomEventEnvelope;

import reactor.core.publisher.Mono;

public interface RoomEventPublisher {
    Mono<Void> publish(String topic, String key, RoomEventEnvelope<?> envelope);
}