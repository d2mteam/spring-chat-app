package com.project.chatservice.infrastructure.websocket;

import java.util.Optional;
import java.util.Set;
import org.springframework.web.socket.WebSocketSession;

public interface SessionRegistry {

    void register(WebSocketSession session, String userId);

    void remove(String sessionId);

    Optional<WebSocketSession> getSession(String sessionId);

    Optional<String> getUserId(String sessionId);

    void subscribe(String sessionId, String destination);

    void unsubscribe(String sessionId, String destination);

    Set<WebSocketSession> getSubscribers(String destination);
}
