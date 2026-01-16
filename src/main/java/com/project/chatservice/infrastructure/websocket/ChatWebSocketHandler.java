package com.project.chatservice.infrastructure.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Represents the chat web socket handler.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketMessageRouter messageRouter;
    private final SessionRegistry sessionRegistry;
    private final SessionSender sessionSender;
    private final WebSocketUserResolver userResolver;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = userResolver.resolveUserId(session);
        sessionSender.register(session);
        sessionRegistry.register(session.getId(), userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        messageRouter.route(session.getId(), message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionSender.remove(session.getId());
        sessionRegistry.remove(session.getId());
    }

}
