package com.project.chatservice.infrastructure.websocket.model;

import jakarta.validation.constraints.NotBlank;

public record AckRequest(@NotBlank String messageId) {
}
