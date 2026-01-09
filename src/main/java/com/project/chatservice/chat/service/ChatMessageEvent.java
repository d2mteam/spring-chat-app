package com.project.chatservice.chat.service;

import java.time.Instant;

/**
 * Represents the chat message event.
 */
public record ChatMessageEvent(Long roomId,
                               Long messageId,
                               String senderId,
                               String content,
                               Long parentId,
                               String contentType,
                               String attachmentUrl,
                               String attachmentName,
                               String attachmentMimeType,
                               Instant createdAt,
                               Instant editedAt,
                               Instant deletedAt,
                               String deletedBy) {
}
