package com.piseth.java.school.roomownerservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.messaging")
public class MessagingProperties {
    private String producerId = "room-owner-service";
    private String eventVersion = "v1";
    private Topics topics = new Topics();

    @Data
    public static class Topics {
        private String roomEvents = "room.events.v1";
        private String roomDlq = "room.events.v1.dlq";
    }
}