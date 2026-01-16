package com.project.chatservice.infrastructure.websocket;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * Represents the in memory session registry.
 */
@Component
public class InMemorySessionRegistry implements SessionRegistry {

    private final ConcurrentHashMap<String, SessionConnection> sessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> userIds = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Set<String>> destinationSubscriptions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Set<String>> sessionSubscriptions = new ConcurrentHashMap<>();

    @Override
    public void register(SessionConnection connection, String userId) {
        sessions.put(connection.id(), connection);
        userIds.put(connection.id(), userId);
        sessionSubscriptions.putIfAbsent(connection.id(), ConcurrentHashMap.newKeySet());
    }

    @Override
    public void remove(String sessionId) {
        sessions.remove(sessionId);
        userIds.remove(sessionId);
        Set<String> destinations = sessionSubscriptions.remove(sessionId);
        if (destinations != null) {
            destinations.forEach(destination -> {
                Set<String> subscribers = destinationSubscriptions.get(destination);
                if (subscribers != null) {
                    subscribers.remove(sessionId);
                    if (subscribers.isEmpty()) {
                        destinationSubscriptions.remove(destination);
                    }
                }
            });
        }
    }

    @Override
    public Optional<SessionConnection> getSession(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    @Override
    public Optional<String> getUserId(String sessionId) {
        return Optional.ofNullable(userIds.get(sessionId));
    }

    @Override
    public void subscribe(String sessionId, String destination) {
        sessionSubscriptions.computeIfAbsent(sessionId, ignored -> ConcurrentHashMap.newKeySet())
            .add(destination);
        destinationSubscriptions.computeIfAbsent(destination, ignored -> ConcurrentHashMap.newKeySet())
            .add(sessionId);
    }

    @Override
    public void unsubscribe(String sessionId, String destination) {
        Set<String> destinations = sessionSubscriptions.get(sessionId);
        if (destinations != null) {
            destinations.remove(destination);
        }
        Set<String> subscribers = destinationSubscriptions.get(destination);
        if (subscribers != null) {
            subscribers.remove(sessionId);
            if (subscribers.isEmpty()) {
                destinationSubscriptions.remove(destination);
            }
        }
    }

    @Override
    public Set<SessionConnection> getSubscribers(String destination) {
        Set<String> sessionIds = destinationSubscriptions.getOrDefault(destination, Collections.emptySet());
        Set<SessionConnection> result = ConcurrentHashMap.newKeySet();
        sessionIds.forEach(id -> {
            SessionConnection connection = sessions.get(id);
            if (connection != null && connection.isOpen()) {
                result.add(connection);
            }
        });
        return result;
    }
}
