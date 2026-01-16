package com.project.chatservice.auth.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Represents auth configuration properties.
 */
@ConfigurationProperties(prefix = "chat.auth")
public record AuthProperties(
    boolean enforce,
    String sessionStore,
    String sessionKeyPrefix,
    JwtProperties jwt
) {

    public record JwtProperties(
        String issuer,
        String secret,
        Duration accessTokenTtl
    ) {
    }
}
