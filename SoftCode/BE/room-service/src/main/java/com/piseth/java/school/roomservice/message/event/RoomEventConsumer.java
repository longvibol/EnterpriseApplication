package com.piseth.java.school.roomservice.message.event;



import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piseth.java.school.roomservice.config.Topics;
import com.piseth.java.school.roomservice.service.RoomProjectionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomEventConsumer {

    private final ReactiveKafkaConsumerTemplate<String, RoomEventEnvelope<?>> consumer;
    private final ObjectMapper objectMapper;
    private final RoomProjectionService projectionService;
    private final Topics topics;

    @EventListener(ApplicationReadyEvent.class)
    public void subscribe() {
        consumer.receiveAutoAck() // topics already set in ReceiverOptions
            .doOnSubscribe(s -> log.info("Subscribed to topic {}", topics.getRoomEvents()))
            .flatMap(this::handleRecord, 1)
            .doOnError(ex -> log.error("Kafka stream error: {}", ex.getMessage(), ex))
            .retry()
            .subscribe();
    }

    private Mono<Void> handleRecord(final ConsumerRecord<String, RoomEventEnvelope<?>> record) {
        try {
            RoomEventEnvelope raw = record.value();

            // 'data' is still a LinkedHashMap (generic erased) -> convert to your payload type
            RoomFullPayload payload = objectMapper.convertValue(raw.getData(), RoomFullPayload.class);

            RoomEventEnvelope<RoomFullPayload> env = new RoomEventEnvelope<>();
            env.setEventId(raw.getEventId());
            env.setVersion(raw.getVersion());
            env.setType(objectMapper.convertValue(raw.getType(), RoomEventType.class));
            env.setOccurredAt(raw.getOccurredAt());
            env.setProducer(raw.getProducer());
            env.setAggregateId(raw.getAggregateId());
            env.setAggregateType(raw.getAggregateType());
            env.setHeaders(raw.getHeaders());
            env.setData(payload);

            return projectionService.apply(env);
        } catch (Exception e) {
            log.error("Failed to process record key={} offset={}: {}", record.key(), record.offset(), e.getMessage(), e);
            // TODO: publish to error topic / DLQ (date letter quer if you maintain one
            return Mono.empty();
        }
    }
}