package com.project.chatservice.infrastructure.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatservice.infrastructure.websocket.model.ServerMessageEnvelope;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Represents the default web socket message sender.
 */
@Component
@RequiredArgsConstructor
@Slf4j
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
        TextMessage textMessage = new TextMessage(payload);
        for (WebSocketSession session : sessionRegistry.getSubscribers(destination)) {
            Object sendLock = sessionRegistry.getSendLock(session.getId()).orElse(session);
            synchronized (sendLock) {
                try {
                    session.sendMessage(textMessage);
                } catch (IOException e) {
                    log.warn("Failed to send websocket message to session {}", session.getId(), e);
                }
            }
        }
    }
}
