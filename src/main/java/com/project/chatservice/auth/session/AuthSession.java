package com.project.chatservice.auth.session;

import java.time.Instant;

/**
 * Represents a user auth session.
 */
public record AuthSession(
    String tokenId,
    String userId,
    String username,
    Instant issuedAt,
    Instant expiresAt
) {
}
