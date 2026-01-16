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
    private final SessionSender sessionSender;

    @Override
    public void send(String destination, ServerMessageEnvelope message) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(message);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to serialize websocket message", e);
        }
        sessionSender.sendToSessions(sessionRegistry.getSubscribers(destination), payload);
    }

    @Override
    public void sendToSession(String sessionId, ServerMessageEnvelope message) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(message);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to serialize websocket message", e);
        }
        sessionSender.sendToSession(sessionId, payload);
    }
}
