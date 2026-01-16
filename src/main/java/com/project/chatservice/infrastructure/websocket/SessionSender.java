package com.project.chatservice.infrastructure.websocket;

import java.util.Set;
import org.springframework.web.socket.WebSocketSession;

/**
 * Represents the session sender responsible for IO transport.
 */
public interface SessionSender {

    void register(WebSocketSession session);

    void remove(String sessionId);

    void sendToSession(String sessionId, String payload);

    void sendToSessions(Set<String> sessionIds, String payload);
}
