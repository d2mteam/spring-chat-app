package com.project.chatservice.infrastructure.websocket;

/**
 * Represents the web socket ack store.
 */
public interface WebSocketAckStore {

    void recordAcknowledgement(String messageId, String userId);
}
