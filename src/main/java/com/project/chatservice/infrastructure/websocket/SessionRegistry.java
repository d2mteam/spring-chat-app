package com.project.chatservice.infrastructure.websocket;

import java.util.Optional;
import java.util.Set;

/**
 * Represents the session registry.
 */
public interface SessionRegistry {

    void register(String sessionId, String userId);

    void remove(String sessionId);

    Optional<String> getUserId(String sessionId);

    void subscribe(String sessionId, String destination);

    void unsubscribe(String sessionId, String destination);

    Set<String> getSubscribers(String destination);
}
