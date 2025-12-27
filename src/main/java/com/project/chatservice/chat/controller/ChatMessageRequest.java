package com.project.chatservice.chat.controller;

public class ChatMessageRequest {

    private String content;
    // Task 2: message reply vào thread nào.
    private Long parentId;
    // Task 6: loại nội dung gửi lên (text/image/file).
    private String contentType;
    // Task 6: URL file/ảnh.
    private String attachmentUrl;
    // Task 6: tên file.
    private String attachmentName;
    // Task 6: MIME type file.
    private String attachmentMimeType;

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
