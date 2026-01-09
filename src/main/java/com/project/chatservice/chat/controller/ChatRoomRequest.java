package com.project.chatservice.chat.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the chat room request.
 */
@Getter
@Setter
@NoArgsConstructor
public class ChatRoomRequest {

    @NotBlank
    private String name;
}
