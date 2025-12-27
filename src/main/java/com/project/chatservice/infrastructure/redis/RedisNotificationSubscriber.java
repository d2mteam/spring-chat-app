package com.project.chatservice.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatservice.chat.service.NotificationEvent;
import com.project.chatservice.infrastructure.websocket.WebSocketBroadcaster;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisNotificationSubscriber implements MessageListener {

    // Task 8: nhận notification từ Redis và broadcast tới user.
    private final ObjectMapper objectMapper;
    private final WebSocketBroadcaster broadcaster;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String payload = new String(message.getBody(), StandardCharsets.UTF_8);
            NotificationEvent event = objectMapper.readValue(payload, NotificationEvent.class);
            broadcaster.broadcastNotification(event);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to handle redis notification", e);
        }
    }
}
