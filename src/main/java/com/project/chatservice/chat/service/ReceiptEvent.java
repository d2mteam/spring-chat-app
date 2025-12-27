package com.project.chatservice.chat.service;

import java.time.Instant;

public record ReceiptEvent(Long roomId,
                           Long messageId,
                           String userId,
                           String status,
                           Instant updatedAt) {
}
