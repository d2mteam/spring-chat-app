package com.project.chatservice.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatservice.chat.service.ChatMessageEvent;
import com.project.chatservice.infrastructure.websocket.WebSocketBroadcaster;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * Represents the redis subscriber.
 */
@Component
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final WebSocketBroadcaster broadcaster;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String payload = new String(message.getBody(), StandardCharsets.UTF_8);
            ChatMessageEvent event = objectMapper.readValue(payload, ChatMessageEvent.class);
            broadcaster.broadcastMessage(event);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to handle redis message", e);
        }
    }
}
