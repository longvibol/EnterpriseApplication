package com.piseth.java.school.roomservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.messaging.topics")
public class Topics {
    private String roomEvents = "room.events.v1";
}