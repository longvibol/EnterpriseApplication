package com.piseth.java.school.roomownerservice.outbox;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("outbox_events")
@CompoundIndexes({
    @CompoundIndex(name = "idx_status_availableAt", def = "{'status': 1, 'availableAt': 1}"),
    @CompoundIndex(name = "idx_aggregate", def = "{'aggregateType': 1, 'aggregateId': 1}")
})
public class OutboxEvent {
    @Id
    private String id;

    @Indexed
    private String aggregateId;
    private String aggregateType;

    private String eventType;
    private String eventVersion;
    private String producer;

    private String key;
    private String topic;
    private Map<String, String> headers;

    private String payloadJson;
    private LocalDateTime occurredAt;

    private OutboxStatus status;
    private int attempt;
    private LocalDateTime availableAt;
    private String lastError;
    private LocalDateTime sentAt;

    // For TTL (we set this when marking SENT)
    @Indexed(expireAfterSeconds = 0)
    private LocalDateTime expireAt;
}