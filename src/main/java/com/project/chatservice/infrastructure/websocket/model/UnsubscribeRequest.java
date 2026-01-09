package com.project.chatservice.infrastructure.websocket.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents the unsubscribe request.
 */
public record UnsubscribeRequest(@NotBlank String destination) {
}
