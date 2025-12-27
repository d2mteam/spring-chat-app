package com.project.chatservice.chat.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageEditRequest {

    @NotBlank
    private String content;

    @NotBlank
    private String editorId;
}
