package com.project.chatservice.auth.application.port;

/**
 * Represents an auth session store.
 */
public interface AuthSessionStore {

    void store(AuthSession session);

    boolean isActive(String tokenId, String userId);

    void revoke(String tokenId);
}
