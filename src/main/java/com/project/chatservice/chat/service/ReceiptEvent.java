package com.project.chatservice.chat.service;

import java.time.Instant;

// Task 1: sự kiện read-receipt để broadcast trạng thái đã đọc.
/**
 * Represents the receipt event.
 */
public record ReceiptEvent(Long roomId,
                           Long messageId,
                           String userId,
                           String status,
                           Instant updatedAt) {
}
