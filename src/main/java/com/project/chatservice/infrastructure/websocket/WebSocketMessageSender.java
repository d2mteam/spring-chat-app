package com.project.chatservice.infrastructure.websocket;

import com.project.chatservice.infrastructure.websocket.model.ServerMessageEnvelope;

/**
 * Represents the web socket message sender.
 */
public interface WebSocketMessageSender {

    void send(String destination, ServerMessageEnvelope message);
}
