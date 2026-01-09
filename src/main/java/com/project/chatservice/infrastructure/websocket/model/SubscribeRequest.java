package com.project.chatservice.infrastructure.websocket.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents the subscribe request.
 */
public record SubscribeRequest(@NotBlank String destination) {
}
