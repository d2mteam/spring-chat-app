package com.project.chatservice.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatservice.chat.service.NotificationEvent;
import com.project.chatservice.config.RedisChannelsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisNotificationPublisher {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final RedisChannelsProperties channelsProperties;

    public void publish(NotificationEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend(channelsProperties.notifications(), payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize notification event", e);
        }
    }
}
