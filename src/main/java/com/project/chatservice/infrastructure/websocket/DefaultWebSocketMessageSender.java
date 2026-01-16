package com.project.chatservice.infrastructure.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatservice.infrastructure.websocket.model.ServerMessageEnvelope;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Represents the default web socket message sender.
 */
@Component
@RequiredArgsConstructor
public class DefaultWebSocketMessageSender implements WebSocketMessageSender {

    private final ObjectMapper objectMapper;
    private final SessionRegistry sessionRegistry;

    @Override
    public void send(String destination, ServerMessageEnvelope message) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(message);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to serialize websocket message", e);
        }
        for (SessionConnection connection : sessionRegistry.getSubscribers(destination)) {
            connection.send(payload);
        }
    }

    @Override
    public void sendToSession(String sessionId, ServerMessageEnvelope message) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(message);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to serialize websocket message", e);
        }
        sessionRegistry.getSession(sessionId).ifPresent(connection -> connection.send(payload));
    }
}
