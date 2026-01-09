package com.project.chatservice.infrastructure.websocket.model;

import jakarta.validation.constraints.NotNull;

/**
 * Represents the mark read request.
 */
public record MarkReadRequest(
    @NotNull Long roomId,
    @NotNull Long messageId
) {
}
