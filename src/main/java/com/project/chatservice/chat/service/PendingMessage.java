package com.project.chatservice.chat.service;

import java.time.Instant;

/**
 * Represents a pending message stored in Redis before persistence.
 */
public record PendingMessage(
    Long roomId,
    String senderId,
    String content,
    Long parentId,
    String contentType,
    String attachmentUrl,
    String attachmentName,
    String attachmentMimeType,
    Instant enqueuedAt
) {
}
