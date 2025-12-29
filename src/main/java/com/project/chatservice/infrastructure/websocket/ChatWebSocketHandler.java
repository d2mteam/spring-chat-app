package com.project.chatservice.infrastructure.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatservice.infrastructure.websocket.model.ClientMessageEnvelope;
import com.project.chatservice.infrastructure.websocket.model.ServerMessageEnvelope;
import com.project.chatservice.infrastructure.websocket.model.ServerMessageType;
import com.project.chatservice.infrastructure.websocket.model.WebSocketErrorResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final WebSocketMessageRouter messageRouter;
    private final SessionRegistry sessionRegistry;
    private final WebSocketUserResolver userResolver;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = userResolver.resolveUserId(session);
        sessionRegistry.register(session, userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            ClientMessageEnvelope envelope = objectMapper.readValue(message.getPayload(), ClientMessageEnvelope.class);
            messageRouter.route(session, envelope);
        } catch (Exception ex) {
            sendError(session, "INVALID_MESSAGE", ex.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionRegistry.remove(session.getId());
    }

    private void sendError(WebSocketSession session, String code, String message) {
        WebSocketErrorResponse payload = new WebSocketErrorResponse(code, message, Instant.now());
        ServerMessageEnvelope envelope = new ServerMessageEnvelope(
            UUID.randomUUID().toString(),
            ServerMessageType.ERROR,
            null,
            payload,
            Instant.now()
        );
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(envelope)));
        } catch (IOException ignored) {
            log.warn("Failed to send websocket error response to session {}", session.getId(), ignored);
        }
    }
}
