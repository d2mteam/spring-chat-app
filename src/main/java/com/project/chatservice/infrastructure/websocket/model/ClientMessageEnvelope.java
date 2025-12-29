package com.project.chatservice.infrastructure.websocket.model;

import com.fasterxml.jackson.databind.JsonNode;

public record ClientMessageEnvelope(
    ClientMessageType type,
    JsonNode payload
) {
}
