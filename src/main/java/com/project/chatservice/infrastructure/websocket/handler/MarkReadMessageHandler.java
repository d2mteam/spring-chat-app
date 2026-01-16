package com.project.chatservice.infrastructure.websocket.handler;

import com.project.chatservice.chat.service.MessageService;
import com.project.chatservice.infrastructure.websocket.SessionContext;
import com.project.chatservice.infrastructure.websocket.WebSocketPayloadValidator;
import com.project.chatservice.infrastructure.websocket.model.ClientMessageType;
import com.project.chatservice.infrastructure.websocket.model.MarkReadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Represents the mark read message handler.
 */
@Component
@RequiredArgsConstructor
public class MarkReadMessageHandler implements ClientMessageHandler {

    private final MessageService messageService;
    private final WebSocketPayloadValidator payloadValidator;

    @Override
    public ClientMessageType supports() {
        return ClientMessageType.MARK_READ;
    }

    @Override
    public Class<?> payloadType() {
        return MarkReadRequest.class;
    }

    @Override
    public void handle(SessionContext context, Object payload) {
        MarkReadRequest request = (MarkReadRequest) payload;
        payloadValidator.validate(request);
        String userId = context.userId();
        messageService.markRead(request.roomId(), request.messageId(), userId);
    }
}
