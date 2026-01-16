package com.project.chatservice.infrastructure.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatservice.infrastructure.websocket.handler.ClientMessageHandler;
import com.project.chatservice.infrastructure.websocket.model.ClientMessageEnvelope;
import com.project.chatservice.infrastructure.websocket.model.ClientMessageType;
import com.project.chatservice.infrastructure.websocket.model.ServerMessageEnvelope;
import com.project.chatservice.infrastructure.websocket.model.ServerMessageType;
import com.project.chatservice.infrastructure.websocket.model.WebSocketErrorResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Represents the web socket message router.
 */
@Component
@Slf4j
public class WebSocketMessageRouter {

    private final ObjectMapper objectMapper;
    private final Map<ClientMessageType, ClientMessageHandler> handlers;
    private final SessionRegistry sessionRegistry;
    private final WebSocketMessageSender messageSender;

    public WebSocketMessageRouter(ObjectMapper objectMapper,
                                  List<ClientMessageHandler> handlers,
                                  SessionRegistry sessionRegistry,
                                  WebSocketMessageSender messageSender) {
        this.objectMapper = objectMapper;
        this.handlers = new EnumMap<>(ClientMessageType.class);
        handlers.forEach(handler -> this.handlers.put(handler.supports(), handler));
        this.sessionRegistry = sessionRegistry;
        this.messageSender = messageSender;
    }

    public void route(String sessionId, String payload) {
        ClientMessageEnvelope envelope;
        try {
            envelope = objectMapper.readValue(payload, ClientMessageEnvelope.class);
        } catch (IOException ex) {
            sendError(sessionId, "INVALID_MESSAGE", ex.getMessage());
            return;
        }
        if (envelope == null || envelope.type() == null) {
            log.debug("Ignoring websocket message with missing type from session {}", sessionId);
            return;
        }
        ClientMessageHandler handler = handlers.get(envelope.type());
        if (handler == null) {
            log.debug("Ignoring websocket message with unsupported type {} from session {}", envelope.type(),
                sessionId);
            return;
        }
        Object convertedPayload = objectMapper.convertValue(envelope.payload(), handler.payloadType());
        if (convertedPayload == null) {
            throw new IllegalArgumentException("Payload is required for message type: " + envelope.type());
        }
        String userId = sessionRegistry.getUserId(sessionId).orElse(null);
        SessionContext context = new SessionContext(sessionId, userId);
        handler.handle(context, convertedPayload);
    }

    private void sendError(String sessionId, String code, String message) {
        WebSocketErrorResponse errorPayload = new WebSocketErrorResponse(code, message, Instant.now());
        ServerMessageEnvelope envelope = new ServerMessageEnvelope(
            1,
            ServerMessageType.ERROR,
            UUID.randomUUID().toString(),
            Instant.now().toEpochMilli(),
            errorPayload
        );
        messageSender.sendToSession(sessionId, envelope);
    }
}
