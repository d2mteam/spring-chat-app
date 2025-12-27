package com.project.chatservice.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatservice.chat.service.NotificationEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisNotificationPublisher {

    // Task 8: kênh pub/sub cho notification mention.
    public static final String CHANNEL = "chat:notifications";

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisNotificationPublisher(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(NotificationEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend(CHANNEL, payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize notification event", e);
        }
    }
}
