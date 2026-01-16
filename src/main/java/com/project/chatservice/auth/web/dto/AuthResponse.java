package com.project.chatservice.auth.web.dto;

import java.time.Instant;

/**
 * Represents the auth response.
 */
public record AuthResponse(
    String accessToken,
    String tokenType,
    Instant expiresAt,
    String userId,
    String username
) {
}
