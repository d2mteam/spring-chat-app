package com.project.chatservice.chat.service;

import java.time.Instant;

// Task 8: sự kiện mention để gửi thông báo realtime cho người dùng được nhắc.
/**
 * Represents the notification event.
 */
public record NotificationEvent(String userId,
                                Long roomId,
                                Long messageId,
                                String type,
                                String preview,
                                Instant createdAt) {
}
