package com.project.chatservice.infrastructure.websocket.model;

import java.time.Instant;

public record ServerMessageEnvelope(
    String id,
    ServerMessageType type,
    String destination,
    Object payload,
    Instant timestamp
) {
}
