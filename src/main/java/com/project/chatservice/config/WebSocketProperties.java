package com.project.chatservice.config;

import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Represents the web socket properties.
 */
@ConfigurationProperties(prefix = "chat.websocket")
public record WebSocketProperties(
    String endpoint,
    List<String> allowedOrigins,
    String anonymousUserId,
    String guestUserPrefix,
    DestinationProperties destinations,
    AckProperties ack
) {

    public record DestinationProperties(
        String rooms,
        String receiptsSuffix,
        String users,
        String notificationsSuffix
    ) {
    }

    public record AckProperties(
        String keyPrefix,
        Duration ttl
    ) {
    }
}
