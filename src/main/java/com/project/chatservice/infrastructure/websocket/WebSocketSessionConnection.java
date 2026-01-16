package com.project.chatservice.infrastructure.websocket;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
final class WebSocketSessionConnection implements SessionConnection {

    private final WebSocketSession session;
    private final Object sendLock = new Object();

    WebSocketSessionConnection(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public String id() {
        return session.getId();
    }

    @Override
    public boolean isOpen() {
        return session.isOpen();
    }

    @Override
    public void send(String payload) {
        TextMessage textMessage = new TextMessage(payload);
        synchronized (sendLock) {
            try {
                session.sendMessage(textMessage);
            } catch (IOException e) {
                log.warn("Failed to send websocket message to session {}", session.getId(), e);
            }
        }
    }
}
