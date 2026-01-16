package com.project.chatservice.infrastructure.websocket;

/**
 * Represents immutable context for a websocket session.
 */
public record SessionContext(String sessionId, String userId) {
}
