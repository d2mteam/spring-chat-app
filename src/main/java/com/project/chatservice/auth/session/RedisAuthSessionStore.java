package com.project.chatservice.auth.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatservice.auth.config.AuthProperties;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Represents the Redis-backed auth session store.
 */
public class RedisAuthSessionStore implements AuthSessionStore {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final AuthProperties properties;

    public RedisAuthSessionStore(RedisTemplate<String, String> redisTemplate,
                                 ObjectMapper objectMapper,
                                 AuthProperties properties) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.properties = properties;
    }

    @Override
    public void store(AuthSession session) {
        String key = key(session.tokenId());
        Duration ttl = Duration.between(Instant.now(), session.expiresAt());
        if (ttl.isNegative() || ttl.isZero()) {
            return;
        }
        try {
            String payload = objectMapper.writeValueAsString(session);
            redisTemplate.opsForValue().set(key, payload, ttl);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to store auth session", e);
        }
    }

    @Override
    public boolean isActive(String tokenId, String userId) {
        String payload = redisTemplate.opsForValue().get(key(tokenId));
        if (payload == null || payload.isBlank()) {
            return false;
        }
        try {
            AuthSession session = objectMapper.readValue(payload, AuthSession.class);
            if (session.expiresAt().isBefore(Instant.now())) {
                revoke(tokenId);
                return false;
            }
            return session.userId().equals(userId);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void revoke(String tokenId) {
        redisTemplate.delete(key(tokenId));
    }

    private String key(String tokenId) {
        return properties.sessionKeyPrefix() + tokenId;
    }
}
