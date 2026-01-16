package com.project.chatservice.auth.jwt;

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
