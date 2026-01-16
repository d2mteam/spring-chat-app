package com.project.chatservice.infrastructure.websocket.handler;

import com.project.chatservice.infrastructure.websocket.SessionContext;
import com.project.chatservice.infrastructure.websocket.SessionRegistry;
import com.project.chatservice.infrastructure.websocket.WebSocketPayloadValidator;
import com.project.chatservice.infrastructure.websocket.WebSocketSubscriptionValidator;
import com.project.chatservice.infrastructure.websocket.model.ClientMessageType;
import com.project.chatservice.infrastructure.websocket.model.SubscribeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Represents the subscribe message handler.
 */
@Component
@RequiredArgsConstructor
public class SubscribeMessageHandler implements ClientMessageHandler {

    private final SessionRegistry sessionRegistry;
    private final WebSocketSubscriptionValidator subscriptionValidator;
    private final WebSocketPayloadValidator payloadValidator;

    @Override
    public ClientMessageType supports() {
        return ClientMessageType.SUBSCRIBE;
    }

    @Override
    public Class<?> payloadType() {
        return SubscribeRequest.class;
    }

    @Override
    public void handle(SessionContext context, Object payload) {
        SubscribeRequest request = (SubscribeRequest) payload;
        payloadValidator.validate(request);
        String destination = request.destination();
        String userId = context.userId();
        if (!subscriptionValidator.isAllowed(destination, userId)) {
            throw new IllegalArgumentException("Subscription not allowed");
        }
        sessionRegistry.subscribe(context.sessionId(), destination);
    }
}
