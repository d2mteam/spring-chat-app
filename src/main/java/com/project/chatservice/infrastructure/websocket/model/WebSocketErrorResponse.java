package com.project.chatservice.infrastructure.websocket.model;

import java.time.Instant;

public record WebSocketErrorResponse(String code, String message, Instant timestamp) {
}
