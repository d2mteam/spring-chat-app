package com.project.chatservice.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatservice.chat.service.ReceiptEvent;
import com.project.chatservice.config.RedisChannelsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisReceiptPublisher {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final RedisChannelsProperties channelsProperties;

    public void publish(ReceiptEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend(channelsProperties.receipts(), payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize receipt event", e);
        }
    }
}
