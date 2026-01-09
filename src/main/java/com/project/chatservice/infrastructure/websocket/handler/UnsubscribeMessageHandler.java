package com.project.chatservice.infrastructure.websocket.handler;

import com.project.chatservice.infrastructure.websocket.SessionRegistry;
import com.project.chatservice.infrastructure.websocket.WebSocketPayloadValidator;
import com.project.chatservice.infrastructure.websocket.model.ClientMessageType;
import com.project.chatservice.infrastructure.websocket.model.UnsubscribeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * Represents the unsubscribe message handler.
 */
@Component
@RequiredArgsConstructor
public class UnsubscribeMessageHandler implements ClientMessageHandler {

    private final SessionRegistry sessionRegistry;
    private final WebSocketPayloadValidator payloadValidator;

    @Override
    public ClientMessageType supports() {
        return ClientMessageType.UNSUBSCRIBE;
    }

    @Override
    public Class<?> payloadType() {
        return UnsubscribeRequest.class;
    }

    @Override
    public void handle(WebSocketSession session, Object payload) {
        UnsubscribeRequest request = (UnsubscribeRequest) payload;
        payloadValidator.validate(request);
        sessionRegistry.unsubscribe(session.getId(), request.destination());
    }
}
