package com.project.chatservice.chat.controller;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents the chat message response.
 */
@Getter
@AllArgsConstructor
public class ChatMessageResponse {

    private final Long id;
    private final Long roomId;
    private final String senderId;
    private final String content;
    // Task 2: parentId để client render thread/reply.
    private final Long parentId;
    // Task 6: loại nội dung trả về.
    private final String contentType;
    // Task 6: URL file/ảnh đính kèm.
    private final String attachmentUrl;
    // Task 6: tên file đính kèm.
    private final String attachmentName;
    // Task 6: MIME type của file.
    private final String attachmentMimeType;
    private final Instant createdAt;
    // Task 5: thời điểm chỉnh sửa.
    private final Instant editedAt;
    // Task 5: thời điểm xóa mềm.
    private final Instant deletedAt;
    // Task 5: người xóa mềm.
    private final String deletedBy;
}
