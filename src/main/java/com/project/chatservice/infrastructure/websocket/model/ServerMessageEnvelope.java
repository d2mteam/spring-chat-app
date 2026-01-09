package com.project.chatservice.infrastructure.websocket.model;

import java.time.Instant;

/**
 * Represents the server message envelope.
 */
public record ServerMessageEnvelope(
    String id,
    ServerMessageType type,
    String destination,
    Object payload,
    Instant timestamp
) {
}
