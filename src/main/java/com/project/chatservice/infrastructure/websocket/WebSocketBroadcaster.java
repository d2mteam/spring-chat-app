package com.project.chatservice.infrastructure.websocket;

import com.project.chatservice.chat.service.ChatMessageEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketBroadcaster {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketBroadcaster(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcast(ChatMessageEvent event) {
        String destination = "/topic/rooms/" + event.roomId();
        messagingTemplate.convertAndSend(destination, event);
    }
}
