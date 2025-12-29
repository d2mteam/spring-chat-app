package com.project.chatservice.infrastructure.websocket.model;

import jakarta.validation.constraints.NotBlank;

public record SubscribeRequest(@NotBlank String destination) {
}
