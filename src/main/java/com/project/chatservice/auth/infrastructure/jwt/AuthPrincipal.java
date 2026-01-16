package com.project.chatservice.auth.infrastructure.jwt;

import java.security.Principal;

/**
 * Represents the authenticated user principal.
 */
public record AuthPrincipal(String userId, String username) implements Principal {

    @Override
    public String getName() {
        return userId;
    }
}
