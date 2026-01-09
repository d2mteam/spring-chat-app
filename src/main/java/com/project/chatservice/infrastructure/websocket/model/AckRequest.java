package com.project.chatservice.infrastructure.websocket.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents the ack request.
 */
public record AckRequest(@NotBlank String messageId) {
}
