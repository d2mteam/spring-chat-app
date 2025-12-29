package com.project.chatservice.infrastructure.websocket;

import com.project.chatservice.infrastructure.websocket.model.ServerMessageEnvelope;

public interface WebSocketMessageSender {

    void send(String destination, ServerMessageEnvelope message);
}
