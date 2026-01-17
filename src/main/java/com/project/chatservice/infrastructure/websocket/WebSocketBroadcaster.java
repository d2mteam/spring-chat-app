package com.project.chatservice.infrastructure.websocket;

import com.project.chatservice.chat.service.ChatMessageEvent;
import com.project.chatservice.chat.service.NotificationEvent;
import com.project.chatservice.chat.service.ReceiptEvent;
import com.project.chatservice.infrastructure.websocket.model.ServerMessageEnvelope;
import com.project.chatservice.infrastructure.websocket.model.ServerMessageType;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Represents the web socket broadcaster.
 */
@Component
@RequiredArgsConstructor
public class WebSocketBroadcaster {

    private final WebSocketMessageSender messageSender;
    private final WebSocketDestinationResolver destinationResolver;

    public void broadcastMessage(ChatMessageEvent event) {
        String destination = destinationResolver.roomMessages(event.roomId());
        messageSender.send(destination, buildEnvelope(ServerMessageType.MESSAGE, event));
    }

    public void broadcastReceipt(ReceiptEvent event) {
        String destination = destinationResolver.roomReceipts(event.roomId());
        messageSender.send(destination, buildEnvelope(ServerMessageType.RECEIPT, event));
    }

    public void broadcastNotification(NotificationEvent event) {
        String destination = destinationResolver.userNotifications(event.userId());
        messageSender.send(destination, buildEnvelope(ServerMessageType.NOTIFICATION, event));
    }

    private ServerMessageEnvelope buildEnvelope(ServerMessageType type, Object payload) {
        return new ServerMessageEnvelope(1, type, UUID.randomUUID().toString(), Instant.now().toEpochMilli(), payload);
    }
}
