package com.project.chatservice.infrastructure.websocket.model;

import jakarta.validation.constraints.NotBlank;

public record UnsubscribeRequest(@NotBlank String destination) {
}
