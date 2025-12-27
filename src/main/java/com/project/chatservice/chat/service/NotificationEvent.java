package com.project.chatservice.chat.service;

import java.time.Instant;

public record NotificationEvent(String userId,
                                Long roomId,
                                Long messageId,
                                String type,
                                String preview,
                                Instant createdAt) {
}
