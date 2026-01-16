package com.project.chatservice.infrastructure.websocket.handler;

import com.project.chatservice.chat.service.MessageService;
import com.project.chatservice.infrastructure.websocket.SessionContext;
import com.project.chatservice.infrastructure.websocket.WebSocketPayloadValidator;
import com.project.chatservice.infrastructure.websocket.model.ClientMessageType;
import com.project.chatservice.infrastructure.websocket.model.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Represents the send chat message handler.
 */
@Component
@RequiredArgsConstructor
public class SendChatMessageHandler implements ClientMessageHandler {

    private final MessageService messageService;
    private final WebSocketPayloadValidator payloadValidator;

    @Override
    public ClientMessageType supports() {
        return ClientMessageType.SEND_MESSAGE;
    }

    @Override
    public Class<?> payloadType() {
        return SendMessageRequest.class;
    }

    @Override
    public void handle(SessionContext context, Object payload) {
        SendMessageRequest request = (SendMessageRequest) payload;
        payloadValidator.validate(request);
        String userId = context.userId();
        messageService.saveAndPublish(
            request.getRoomId(),
            userId,
            request.getContent(),
            request.getParentId(),
            request.getContentType(),
            request.getAttachmentUrl(),
            request.getAttachmentName(),
            request.getAttachmentMimeType()
        );
    }

}
