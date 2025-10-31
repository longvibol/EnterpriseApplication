package com.piseth.java.school.roomownerservice.messaging;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;

import com.piseth.java.school.roomownerservice.messaging.event.RoomEventEnvelope;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class KafkaRoomEventPublisher implements RoomEventPublisher {

    private final ReactiveKafkaProducerTemplate<String, Object> reactiveTemplate;

    @Override
    public Mono<Void> publish(final String topic, final String key, final RoomEventEnvelope<?> envelope) {
        final ProducerRecord<String, Object> record = new ProducerRecord<>(topic, key, envelope);

        record.headers().add(new RecordHeader("eventId", envelope.getEventId().getBytes(StandardCharsets.UTF_8)));
        record.headers().add(new RecordHeader("eventType", envelope.getType().name().getBytes(StandardCharsets.UTF_8)));
        record.headers().add(new RecordHeader("eventVersion", envelope.getVersion().getBytes(StandardCharsets.UTF_8)));
        record.headers().add(new RecordHeader("producer", envelope.getProducer().getBytes(StandardCharsets.UTF_8)));

        if (envelope.getHeaders() != null) {
            envelope.getHeaders().forEach((k, v) -> {
                if (k != null && v != null) {
                    record.headers().add(new RecordHeader(k, v.getBytes(StandardCharsets.UTF_8)));
                }
            });
        }

        return reactiveTemplate.send(record)
                .doOnError(ex -> {
                    // Let caller handle via onErrorResume; we still log for observability
                    // (Outbox worker will retry with backoff)
                })
                .then();
    }
}