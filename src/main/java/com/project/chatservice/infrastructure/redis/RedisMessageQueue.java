package com.project.chatservice.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatservice.chat.service.PendingMessage;
import com.project.chatservice.config.RedisQueueProperties;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Represents the Redis-backed pending message queue.
 */
@Component
@RequiredArgsConstructor
public class RedisMessageQueue {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final RedisQueueProperties queueProperties;

    public void enqueue(PendingMessage message) {
        try {
            redisTemplate.opsForList().rightPush(queueProperties.messageQueueKey(),
                objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to enqueue pending message", e);
        }
    }

    public Optional<PendingMessage> dequeue() {
        String payload = redisTemplate.opsForList().leftPop(queueProperties.messageQueueKey());
        if (payload == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(payload, PendingMessage.class));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse pending message", e);
        }
    }
}
