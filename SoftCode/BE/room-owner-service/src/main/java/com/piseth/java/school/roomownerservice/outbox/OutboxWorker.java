package com.piseth.java.school.roomownerservice.outbox;


import java.time.LocalDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piseth.java.school.roomownerservice.config.OutboxProperties;
import com.piseth.java.school.roomownerservice.messaging.RoomEventPublisher;
import com.piseth.java.school.roomownerservice.messaging.event.RoomEventEnvelope;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@EnableScheduling // to setup the time for schedule senting 
@RequiredArgsConstructor
@Slf4j
public class OutboxWorker {

    private final OutboxRepository repo;
    private final RoomEventPublisher publisher;
    private final ObjectMapper objectMapper;
    private final OutboxProperties props;

    @Scheduled(fixedDelayString = "${app.outbox.poll-interval-ms:1000}")
    public void drainOutbox() {
        repo.findByStatusAndAvailableAtLessThanEqualOrderByOccurredAtAsc(
                    OutboxStatus.PENDING, LocalDateTime.now(), PageRequest.of(0, props.getMaxBatch()))
            .flatMap(this::tryPublish, 1) // sequential to preserve ordering per key; adjust if you shard
            .onErrorContinue((ex, obj) -> log.error("Outbox worker error: {}", ex.getMessage(), ex))
            .subscribe();
    }

    private Mono<OutboxEvent> tryPublish(final OutboxEvent e) {
        final RoomEventEnvelope<?> envelope;
        try {
            envelope = objectMapper.readValue(e.getPayloadJson(), RoomEventEnvelope.class);
        } catch (Exception ex) {
            log.error("Outbox deserialization failed id={}", e.getId(), ex);
            return markFailed(e, ex);
        }

        return publisher.publish(e.getTopic(), e.getKey(), envelope)
                .then(markSent(e))
                .onErrorResume(ex -> onPublishFailed(e, ex));
    }

    private Mono<OutboxEvent> markSent(final OutboxEvent e) {
        e.setStatus(OutboxStatus.SENT);
        e.setSentAt(LocalDateTime.now());
        e.setLastError(null);
        // TTL date
        e.setExpireAt(LocalDateTime.now().plusDays(props.getRetentionDays()));
        return repo.save(e);
    }

    private Mono<OutboxEvent> onPublishFailed(final OutboxEvent e, final Throwable ex) {
        log.warn("Outbox publish failed id={}, attempt={}, error={}", e.getId(), e.getAttempt(), ex.toString());
        final int next = e.getAttempt() + 1;
        e.setAttempt(next);
        e.setLastError(ex.toString());
        if (next >= props.getMaxAttempts()) {
            e.setStatus(OutboxStatus.FAILED);
            return repo.save(e);
        } else {
            final long delaySec = Math.min(300, (long) Math.pow(2, next)); // cap 5 min
            e.setAvailableAt(LocalDateTime.now().plusSeconds(delaySec));
            e.setStatus(OutboxStatus.PENDING);
            return repo.save(e);
        }
    }

    private Mono<OutboxEvent> markFailed(final OutboxEvent e, final Throwable ex) {
        e.setStatus(OutboxStatus.FAILED);
        e.setLastError(ex.toString());
        return repo.save(e);
    }
}