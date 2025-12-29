package com.project.chatservice.infrastructure.websocket;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class InMemorySessionRegistry implements SessionRegistry {

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> userIds = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Set<String>> destinationSubscriptions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Set<String>> sessionSubscriptions = new ConcurrentHashMap<>();

    @Override
    public void register(WebSocketSession session, String userId) {
        sessions.put(session.getId(), session);
        userIds.put(session.getId(), userId);
        sessionSubscriptions.putIfAbsent(session.getId(), ConcurrentHashMap.newKeySet());
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
    public Optional<WebSocketSession> getSession(String sessionId) {
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
    public Set<WebSocketSession> getSubscribers(String destination) {
        Set<String> sessionIds = destinationSubscriptions.getOrDefault(destination, Collections.emptySet());
        Set<WebSocketSession> result = ConcurrentHashMap.newKeySet();
        sessionIds.forEach(id -> {
            WebSocketSession session = sessions.get(id);
            if (session != null && session.isOpen()) {
                result.add(session);
            }
        });
        return result;
    }
}
