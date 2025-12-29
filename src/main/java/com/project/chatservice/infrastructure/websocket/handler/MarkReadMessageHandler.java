package com.project.chatservice.infrastructure.websocket.handler;

import com.project.chatservice.chat.service.MessageService;
import com.project.chatservice.infrastructure.websocket.WebSocketPayloadValidator;
import com.project.chatservice.infrastructure.websocket.WebSocketUserResolver;
import com.project.chatservice.infrastructure.websocket.model.ClientMessageType;
import com.project.chatservice.infrastructure.websocket.model.MarkReadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
@RequiredArgsConstructor
public class MarkReadMessageHandler implements ClientMessageHandler {

    private final MessageService messageService;
    private final WebSocketUserResolver userResolver;
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
    public void handle(WebSocketSession session, Object payload) {
        MarkReadRequest request = (MarkReadRequest) payload;
        payloadValidator.validate(request);
        String userId = userResolver.resolveUserId(session);
        messageService.markRead(request.roomId(), request.messageId(), userId);
    }
}
