package com.project.chatservice.auth.infrastructure.jwt;

import java.time.Instant;

/**
 * Represents parsed JWT claims.
 */
public record JwtClaims(
    String userId,
    String username,
    String tokenId,
    Instant expiresAt
) {
}
