package com.project.chatservice.infrastructure.websocket;

/**
 * Represents an infra-level WebSocket connection wrapper.
 */
public interface SessionConnection {

    String id();

    boolean isOpen();

    void send(String payload);
}
