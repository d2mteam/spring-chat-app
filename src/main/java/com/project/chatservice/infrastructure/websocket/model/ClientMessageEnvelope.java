package com.project.chatservice.infrastructure.websocket.model;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Represents the client message envelope.
 */
public record ClientMessageEnvelope(
    int v,
    ClientMessageType type,
    String id,
    Long ts,
    JsonNode payload
) {
}
