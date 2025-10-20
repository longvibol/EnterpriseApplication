package com.piseth.java.school.roomownerservice.config;

import java.util.Map;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;

import reactor.kafka.sender.SenderOptions;

@Configuration
public class KafkaReactiveConfig {

    @Bean
    ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducerTemplate(
            final KafkaProperties properties) {

        final Map<String, Object> producerProps = properties.buildProducerProperties();
        final SenderOptions<String, Object> senderOptions = SenderOptions.create(producerProps);
        return new ReactiveKafkaProducerTemplate<>(senderOptions);
    }
}