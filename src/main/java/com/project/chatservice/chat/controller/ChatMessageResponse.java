package com.project.chatservice.chat.controller;

import java.time.Instant;

public class ChatMessageResponse {

    private final Long id;
    private final Long roomId;
    private final String senderId;
    private final String content;
    private final Instant createdAt;

    public ChatMessageResponse(Long id, Long roomId, String senderId, String content, Instant createdAt) {
        this.id = id;
        this.roomId = roomId;
        this.senderId = senderId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
