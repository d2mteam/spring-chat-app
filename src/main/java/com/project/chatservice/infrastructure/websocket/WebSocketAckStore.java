package com.project.chatservice.infrastructure.websocket;

public interface WebSocketAckStore {

    void recordAcknowledgement(String messageId, String userId);
}
