package com.piseth.java.school.roomservice.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;

import com.piseth.java.school.roomservice.message.event.RoomEventEnvelope;

import reactor.kafka.receiver.ReceiverOptions;

@Configuration
public class KafkaReactiveConfig {

    @Bean
    ReactiveKafkaConsumerTemplate<String, RoomEventEnvelope<?>> reactiveKafkaConsumerTemplate(
            final KafkaProperties properties, final Topics topics) {

        Map<String, Object> consumerProps = properties.buildConsumerProperties();

        ReceiverOptions<String, RoomEventEnvelope<?>> receiverOptions =
                ReceiverOptions.<String, RoomEventEnvelope<?>>create(consumerProps)
                        .subscription(List.of(topics.getRoomEvents()));

        return new ReactiveKafkaConsumerTemplate<>(receiverOptions);
    }
}