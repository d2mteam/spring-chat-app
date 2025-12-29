package com.project.chatservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "chat.redis.channels")
public record RedisChannelsProperties(
    String messages,
    String receipts,
    String notifications
) {
}
