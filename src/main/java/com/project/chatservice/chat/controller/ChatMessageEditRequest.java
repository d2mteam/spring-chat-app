package com.project.chatservice.chat.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the chat message edit request.
 */
@Getter
@Setter
@NoArgsConstructor
public class ChatMessageEditRequest {

    @NotBlank
    private String content;

    @NotBlank
    private String editorId;
}
