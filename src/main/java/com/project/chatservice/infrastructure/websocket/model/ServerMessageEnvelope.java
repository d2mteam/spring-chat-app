package com.project.chatservice.infrastructure.websocket.model;

/**
 * Represents the server message envelope.
 */
public record ServerMessageEnvelope(
    int v,
    ServerMessageType type,
    String id,
    long ts,
    Object payload
) {
}
