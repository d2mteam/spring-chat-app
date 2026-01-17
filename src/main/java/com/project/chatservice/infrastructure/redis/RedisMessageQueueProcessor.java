package com.project.chatservice.infrastructure.redis;

import com.project.chatservice.chat.service.MessageService;
import com.project.chatservice.config.RedisQueueProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Represents the background processor for pending messages.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisMessageQueueProcessor {

    private final RedisMessageQueue messageQueue;
    private final MessageService messageService;
    private final RedisQueueProperties queueProperties;

    @Scheduled(fixedDelayString = "${chat.redis.queue.poll-delay:500ms}")
    public void drainQueue() {
        int batchSize = Math.max(1, queueProperties.batchSize());
        for (int i = 0; i < batchSize; i++) {
            var pendingMessage = messageQueue.dequeue();
            if (pendingMessage.isEmpty()) {
                return;
            }
            try {
                messageService.persistQueuedMessage(pendingMessage.get());
            } catch (RuntimeException ex) {
                log.warn("Failed to persist queued message, re-enqueuing", ex);
                messageQueue.enqueue(pendingMessage.get());
                return;
            }
        }
    }
}
