package com.project.chatservice.infrastructure.websocket.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents the auth request payload.
 */
public record AuthRequest(
    @NotBlank String token
) {
}
