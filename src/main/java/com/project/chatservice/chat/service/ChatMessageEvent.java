package com.project.chatservice.chat.service;

import java.time.Instant;

public record ChatMessageEvent(Long roomId, Long messageId, String senderId, String content, Instant createdAt) {
}
