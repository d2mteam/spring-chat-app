package com.project.chatservice.infrastructure.websocket;

import com.project.chatservice.config.WebSocketProperties;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Represents the redis web socket ack store.
 */
@Component
@RequiredArgsConstructor
public class RedisWebSocketAckStore implements WebSocketAckStore {

    private final RedisTemplate<String, String> redisTemplate;
    private final WebSocketProperties webSocketProperties;

    @Override
    public void recordAcknowledgement(String messageId, String userId) {
        String key = webSocketProperties.ack().keyPrefix() + messageId + ":" + userId;
        Duration ttl = webSocketProperties.ack().ttl();
        redisTemplate.opsForValue().set(key, Instant.now().toString(), ttl);
    }
}
