package com.project.chatservice.infrastructure.websocket.model;

import jakarta.validation.constraints.NotNull;

public record MarkReadRequest(
    @NotNull Long roomId,
    @NotNull Long messageId
) {
}
