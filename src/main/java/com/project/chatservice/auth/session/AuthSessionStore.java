package com.project.chatservice.auth.session;

/**
 * Represents an auth session store.
 */
public interface AuthSessionStore {

    void store(AuthSession session);

    boolean isActive(String tokenId, String userId);

    void revoke(String tokenId);
}
