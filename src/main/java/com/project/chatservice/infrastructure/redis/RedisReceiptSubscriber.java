package com.project.chatservice.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatservice.chat.service.ReceiptEvent;
import com.project.chatservice.infrastructure.websocket.WebSocketBroadcaster;
import java.nio.charset.StandardCharsets;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisReceiptSubscriber implements MessageListener {

    // Task 1: nhận read receipt từ Redis và broadcast tới room.
    private final ObjectMapper objectMapper;
    private final WebSocketBroadcaster broadcaster;

    public RedisReceiptSubscriber(ObjectMapper objectMapper, WebSocketBroadcaster broadcaster) {
        this.objectMapper = objectMapper;
        this.broadcaster = broadcaster;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String payload = new String(message.getBody(), StandardCharsets.UTF_8);
            ReceiptEvent event = objectMapper.readValue(payload, ReceiptEvent.class);
            broadcaster.broadcastReceipt(event);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to handle redis receipt", e);
        }
    }
}
