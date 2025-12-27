package com.project.chatservice.chat.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageRequest {

    @NotBlank
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
}
