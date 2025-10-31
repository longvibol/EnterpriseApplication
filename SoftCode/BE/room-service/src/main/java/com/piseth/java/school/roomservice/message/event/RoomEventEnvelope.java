package com.piseth.java.school.roomservice.message.event;

import java.time.Instant;
import java.util.Map;

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
    private String producer;
    private String aggregateId;    // room id
    private String aggregateType;  // "Room"
    private Map<String, String> headers;
    private T data;
}