package com.project.chatservice.infrastructure.websocket.handler;

import com.project.chatservice.infrastructure.websocket.SessionContext;
import com.project.chatservice.infrastructure.websocket.WebSocketAckStore;
import com.project.chatservice.infrastructure.websocket.WebSocketPayloadValidator;
import com.project.chatservice.infrastructure.websocket.model.AckRequest;
import com.project.chatservice.infrastructure.websocket.model.ClientMessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Represents the ack message handler.
 */
@Component
@RequiredArgsConstructor
public class AckMessageHandler implements ClientMessageHandler {

    private final WebSocketAckStore ackStore;
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
    public void handle(SessionContext context, Object payload) {
        AckRequest request = (AckRequest) payload;
        payloadValidator.validate(request);
        String userId = context.userId();
        ackStore.recordAcknowledgement(request.messageId(), userId);
    }
}
