package com.project.chatservice.infrastructure.websocket.handler;

import com.project.chatservice.infrastructure.websocket.WebSocketAckStore;
import com.project.chatservice.infrastructure.websocket.WebSocketPayloadValidator;
import com.project.chatservice.infrastructure.websocket.WebSocketUserResolver;
import com.project.chatservice.infrastructure.websocket.model.AckRequest;
import com.project.chatservice.infrastructure.websocket.model.ClientMessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * Represents the ack message handler.
 */
@Component
@RequiredArgsConstructor
public class AckMessageHandler implements ClientMessageHandler {

    private final WebSocketAckStore ackStore;
    private final WebSocketUserResolver userResolver;
    private final WebSocketPayloadValidator payloadValidator;

    @Override
    public ClientMessageType supports() {
        return ClientMessageType.ACK;
    }

    @Override
    public Class<?> payloadType() {
        return AckRequest.class;
    }

    @Override
    public void handle(WebSocketSession session, Object payload) {
        AckRequest request = (AckRequest) payload;
        payloadValidator.validate(request);
        String userId = userResolver.resolveUserId(session);
        ackStore.recordAcknowledgement(request.messageId(), userId);
    }
}
