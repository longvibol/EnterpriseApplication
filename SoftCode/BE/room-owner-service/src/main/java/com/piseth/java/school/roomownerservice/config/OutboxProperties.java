package com.piseth.java.school.roomownerservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.outbox")
public class OutboxProperties {
    private long pollIntervalMs = 1000;
    private int maxBatch = 100;
    private int maxAttempts = 12;
    private int retentionDays = 14; // TTL after SENT
}