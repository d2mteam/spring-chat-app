package com.project.chatservice.chat.controller;

import com.project.chatservice.chat.service.ChatMessageEvent;
import com.project.chatservice.chat.service.MessageService;
import com.project.chatservice.security.CurrentUserProvider;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    private final MessageService messageService;
    private final CurrentUserProvider currentUserProvider;

    public ChatWebSocketController(MessageService messageService, CurrentUserProvider currentUserProvider) {
        this.messageService = messageService;
        this.currentUserProvider = currentUserProvider;
    }

    @MessageMapping("/rooms/{roomId}/send")
    public ChatMessageEvent sendMessage(@DestinationVariable Long roomId,
                                        @Payload ChatMessageRequest request) {
        String senderId = currentUserProvider.getUserId();
        return messageService.saveAndPublish(
            roomId,
            senderId,
            request.getContent(),
            request.getParentId(),
            request.getContentType(),
            request.getAttachmentUrl(),
            request.getAttachmentName(),
            request.getAttachmentMimeType()
        );
    }
}
