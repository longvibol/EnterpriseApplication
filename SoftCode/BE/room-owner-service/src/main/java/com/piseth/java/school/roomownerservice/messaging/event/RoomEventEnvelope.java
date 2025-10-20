package com.piseth.java.school.roomownerservice.messaging.event;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomEventEnvelope<T> {
    private String eventId;
    private String version;
    private RoomEventType type;
    private Instant occurredAt;
    private String producer; // which service is the sender : RoomOwner
    private String aggregateId;   // room id
    private String aggregateType; // "Room"
    private Map<String, String> headers;
    private T data;

    public static <T> RoomEventEnvelope<T> of(
            final RoomEventType type,
            final String version,
            final String producer,
            final String aggregateId,
            final T data,
            final Map<String, String> headers) {

        return RoomEventEnvelope.<T>builder()
                .eventId(UUID.randomUUID().toString())
                .version(version)
                .type(type)
                .occurredAt(Instant.now())
                .producer(producer)
                .aggregateId(aggregateId)
                .aggregateType("Room")
                .headers(headers)
                .data(data)
                .build();
    }
}