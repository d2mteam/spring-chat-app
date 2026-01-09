package com.project.chatservice.infrastructure.websocket.model;

import java.time.Instant;

/**
 * Represents the web socket error response.
 */
public record WebSocketErrorResponse(String code, String message, Instant timestamp) {
}
