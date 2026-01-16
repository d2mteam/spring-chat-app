package com.project.chatservice.auth.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents the login request.
 */
public record LoginRequest(
    @NotBlank String username,
    @NotBlank String password
) {
}
