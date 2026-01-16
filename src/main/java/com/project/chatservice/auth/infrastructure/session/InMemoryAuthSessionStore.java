package com.project.chatservice.auth.infrastructure.session;

import com.project.chatservice.auth.application.port.AuthSessionStore;
import com.project.chatservice.auth.domain.AuthSession;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents an in-memory auth session store.
 */
public class InMemoryAuthSessionStore implements AuthSessionStore {

    private final Map<String, AuthSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void store(AuthSession session) {
        sessions.put(session.tokenId(), session);
    }

    @Override
    public boolean isActive(String tokenId, String userId) {
        AuthSession session = sessions.get(tokenId);
        if (session == null) {
            return false;
        }
        if (session.expiresAt().isBefore(Instant.now())) {
            sessions.remove(tokenId);
            return false;
        }
        return session.userId().equals(userId);
    }

    @Override
    public void revoke(String tokenId) {
        sessions.remove(tokenId);
    }
}
