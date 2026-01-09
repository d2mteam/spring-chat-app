package com.project.chatservice.infrastructure.websocket.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Represents the send message request.
 */
public class SendMessageRequest {

    @NotNull
    private Long roomId;

    @NotBlank
    private String content;

    private Long parentId;
    private String contentType;
    private String attachmentUrl;
    private String attachmentName;
    private String attachmentMimeType;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentMimeType() {
        return attachmentMimeType;
    }

    public void setAttachmentMimeType(String attachmentMimeType) {
        this.attachmentMimeType = attachmentMimeType;
    }
}
