package com.project.chatservice.chat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom room;

    @Column(nullable = false)
    private String senderId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column
    // Task 2: ID của message cha để tạo thread/reply.
    private Long parentId;

    @Column(nullable = false)
    // Task 6: loại nội dung (text, image, file, ...).
    private String contentType = "text";

    @Column(columnDefinition = "TEXT")
    // Task 6: URL file/ảnh đính kèm.
    private String attachmentUrl;

    @Column
    // Task 6: tên file đính kèm để hiển thị.
    private String attachmentName;

    @Column
    // Task 6: MIME type của file đính kèm.
    private String attachmentMimeType;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column
    // Task 5: thời điểm chỉnh sửa nội dung.
    private Instant editedAt;

    @Column
    // Task 5: thời điểm xóa mềm.
    private Instant deletedAt;

    @Column
    // Task 5: userId thực hiện xóa mềm.
    private String deletedBy;

    protected Message() {
    }

    public Message(ChatRoom room,
                   String senderId,
                   String content,
                   Long parentId,
                   String contentType,
                   String attachmentUrl,
                   String attachmentName,
                   String attachmentMimeType) {
        this.room = room;
        this.senderId = senderId;
        this.content = content;
        this.parentId = parentId;
        if (contentType != null) {
            this.contentType = contentType;
        }
        this.attachmentUrl = attachmentUrl;
        this.attachmentName = attachmentName;
        this.attachmentMimeType = attachmentMimeType;
    }

    public Long getId() {
        return id;
    }

    public ChatRoom getRoom() {
        return room;
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

    public void editContent(String newContent) {
        this.content = newContent;
        this.editedAt = Instant.now();
    }

    public void softDelete(String userId) {
        this.deletedAt = Instant.now();
        this.deletedBy = userId;
    }
}
