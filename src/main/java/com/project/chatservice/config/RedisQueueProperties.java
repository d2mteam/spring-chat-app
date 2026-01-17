package com.project.chatservice.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Represents redis queue configuration.
 */
@ConfigurationProperties(prefix = "chat.redis.queue")
public record RedisQueueProperties(
    String messageQueueKey,
    int batchSize,
    Duration pollDelay
) {
}
