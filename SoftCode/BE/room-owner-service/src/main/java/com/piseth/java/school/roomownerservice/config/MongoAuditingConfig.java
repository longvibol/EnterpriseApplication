package com.piseth.java.school.roomownerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMongoAuditing
public class MongoAuditingConfig {

    @Bean
    ReactiveAuditorAware<String> reactiveAuditorAware() {
        return () -> Mono.just("system"); // TODO integrate Keycloak principal
    }
}