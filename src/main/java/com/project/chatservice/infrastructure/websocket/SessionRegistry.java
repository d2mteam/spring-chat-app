package com.project.chatservice.infrastructure.websocket;

import java.util.Optional;
import java.util.Set;

/**
 * Represents the session registry.
 */
public interface SessionRegistry {

    void register(SessionConnection connection, String userId);

    void remove(String sessionId);

    Optional<SessionConnection> getSession(String sessionId);

    Optional<String> getUserId(String sessionId);

    void subscribe(String sessionId, String destination);

    void unsubscribe(String sessionId, String destination);

    Set<SessionConnection> getSubscribers(String destination);
}
