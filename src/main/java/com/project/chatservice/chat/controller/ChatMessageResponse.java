package com.project.chatservice.chat.controller;

import java.time.Instant;

public class ChatMessageResponse {

    private final Long id;
    private final Long roomId;
    private final String senderId;
    private final String content;
    private final Long parentId;
    private final String contentType;
    private final String attachmentUrl;
    private final String attachmentName;
    private final String attachmentMimeType;
    private final Instant createdAt;
    private final Instant editedAt;
    private final Instant deletedAt;
    private final String deletedBy;

    public ChatMessageResponse(Long id,
                               Long roomId,
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
        this.id = id;
        this.roomId = roomId;
        this.senderId = senderId;
        this.content = content;
        this.parentId = parentId;
        this.contentType = contentType;
        this.attachmentUrl = attachmentUrl;
        this.attachmentName = attachmentName;
        this.attachmentMimeType = attachmentMimeType;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
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

    public Long getParentId() {
        return parentId;
    }

    public String getContentType() {
        return contentType;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public String getAttachmentMimeType() {
        return attachmentMimeType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getEditedAt() {
        return editedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public String getDeletedBy() {
        return deletedBy;
    }
}
