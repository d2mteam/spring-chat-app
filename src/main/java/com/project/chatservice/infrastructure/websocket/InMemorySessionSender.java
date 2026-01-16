package com.project.chatservice.infrastructure.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Represents the in memory session sender.
 */
@Component
@Slf4j
public class InMemorySessionSender implements SessionSender {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, Object> sendLocks = new ConcurrentHashMap<>();

    @Override
    public void register(WebSocketSession session) {
        sessions.put(session.getId(), session);
        sendLocks.putIfAbsent(session.getId(), new Object());
    }

    @Override
    public void remove(String sessionId) {
        sessions.remove(sessionId);
        sendLocks.remove(sessionId);
    }

    @Override
    public void sendToSession(String sessionId, String payload) {
        WebSocketSession session = sessions.get(sessionId);
        if (session == null || !session.isOpen()) {
            return;
        }
        send(session, payload);
    }

    @Override
    public void sendToSessions(Set<String> sessionIds, String payload) {
        for (String sessionId : sessionIds) {
            WebSocketSession session = sessions.get(sessionId);
            if (session != null && session.isOpen()) {
                send(session, payload);
            }
        }
    }

    private void send(WebSocketSession session, String payload) {
        Object sendLock = sendLocks.getOrDefault(session.getId(), session);
        synchronized (sendLock) {
            try {
                session.sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                log.warn("Failed to send websocket message to session {}", session.getId(), e);
            }
        }
    }
}
