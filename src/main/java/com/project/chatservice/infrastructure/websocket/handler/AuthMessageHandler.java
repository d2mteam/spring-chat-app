package com.project.chatservice.infrastructure.websocket.handler;

import com.project.chatservice.infrastructure.websocket.SessionContext;
import com.project.chatservice.infrastructure.websocket.SessionRegistry;
import com.project.chatservice.infrastructure.websocket.WebSocketMessageSender;
import com.project.chatservice.infrastructure.websocket.WebSocketPayloadValidator;
import com.project.chatservice.infrastructure.websocket.model.AuthRequest;
import com.project.chatservice.infrastructure.websocket.model.AuthResponse;
import com.project.chatservice.infrastructure.websocket.model.ClientMessageType;
import com.project.chatservice.infrastructure.websocket.model.ServerMessageEnvelope;
import com.project.chatservice.infrastructure.websocket.model.ServerMessageType;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Represents the auth message handler.
 */
@Component
@RequiredArgsConstructor
public class AuthMessageHandler implements ClientMessageHandler {

    private final SessionRegistry sessionRegistry;
    private final WebSocketPayloadValidator payloadValidator;
    private final WebSocketMessageSender messageSender;

    @Override
    public ClientMessageType supports() {
        return ClientMessageType.AUTH;
    }

    @Override
    public Class<?> payloadType() {
        return AuthRequest.class;
    }

    @Override
    public void handle(SessionContext context, Object payload) {
        AuthRequest request = (AuthRequest) payload;
        payloadValidator.validate(request);
        if (sessionRegistry.getUserId(context.sessionId()).isEmpty()) {
            String userId = stripBearerPrefix(request.token());
            sessionRegistry.register(context.sessionId(), userId);
        }
        sendAuthSuccess(context.sessionId());
    }

    private String stripBearerPrefix(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring("Bearer ".length());
        }
        return token;
    }

    private void sendAuthSuccess(String sessionId) {
        AuthResponse response = new AuthResponse(sessionId);
        ServerMessageEnvelope envelope = new ServerMessageEnvelope(
            1,
            ServerMessageType.AUTH,
            UUID.randomUUID().toString(),
            Instant.now().toEpochMilli(),
            response
        );
        messageSender.sendToSession(sessionId, envelope);
    }
}
