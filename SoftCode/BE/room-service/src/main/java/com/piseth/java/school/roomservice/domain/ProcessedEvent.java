package com.piseth.java.school.roomservice.domain;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("processed_events")
@CompoundIndexes({
    @CompoundIndex(name = "idx_agg_unique", def = "{'aggregateType': 1, 'aggregateId': 1}", unique = true)
})
public class ProcessedEvent {
// this class to check the process of event that we have done 
    @Id
    private String id;

    private String aggregateType; // "Room"
    private String aggregateId;   // room id
    private String lastEventId;
    private Instant lastOccurredAt;
}