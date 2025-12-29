package com.project.chatservice.infrastructure.websocket.handler;

import com.project.chatservice.infrastructure.websocket.SessionRegistry;
import com.project.chatservice.infrastructure.websocket.WebSocketPayloadValidator;
import com.project.chatservice.infrastructure.websocket.WebSocketSubscriptionValidator;
import com.project.chatservice.infrastructure.websocket.WebSocketUserResolver;
import com.project.chatservice.infrastructure.websocket.model.ClientMessageType;
import com.project.chatservice.infrastructure.websocket.model.SubscribeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
@RequiredArgsConstructor
public class SubscribeMessageHandler implements ClientMessageHandler {

    private final SessionRegistry sessionRegistry;
    private final WebSocketUserResolver userResolver;
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
    public void handle(WebSocketSession session, Object payload) {
        SubscribeRequest request = (SubscribeRequest) payload;
        payloadValidator.validate(request);
        String destination = request.destination();
        String userId = userResolver.resolveUserId(session);
        if (!subscriptionValidator.isAllowed(destination, userId)) {
            throw new IllegalArgumentException("Subscription not allowed");
        }
        sessionRegistry.subscribe(session.getId(), destination);
    }
}
