package com.project.chatservice.infrastructure.websocket;

import com.project.chatservice.chat.service.ChatMessageEvent;
import com.project.chatservice.chat.service.NotificationEvent;
import com.project.chatservice.chat.service.ReceiptEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketBroadcaster {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketBroadcaster(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastMessage(ChatMessageEvent event) {
        String destination = "/topic/rooms/" + event.roomId();
        messagingTemplate.convertAndSend(destination, event);
    }

    public void broadcastReceipt(ReceiptEvent event) {
        // Task 1: broadcast read receipts theo room.
        String destination = "/topic/rooms/" + event.roomId() + "/receipts";
        messagingTemplate.convertAndSend(destination, event);
    }

    public void broadcastNotification(NotificationEvent event) {
        // Task 8: broadcast notification riêng theo user.
        String destination = "/topic/users/" + event.userId() + "/notifications";
        messagingTemplate.convertAndSend(destination, event);
    }
}
