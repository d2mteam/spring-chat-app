package com.project.demoappchat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    // Cách 1: Dùng @SendTo
    // Client gửi tới: /app/chat.sendMessage
    // Server sẽ forward kết quả tới tất cả client đang sub: /topic/public
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Map<String, Object> sendMessage(@Payload Map<String, Object> chatMessage) {
        log.debug("Sending message: {}", chatMessage.toString());
        return chatMessage;
    }

    // Cách 2: Dùng SimpMessagingTemplate (Linh hoạt hơn)
    // Dùng khi bạn muốn gửi tin nhắn từ Service layer hoặc một sự kiện bất kỳ (ko phải từ user request)
    public void sendNotification(String userId, String message) {
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, message);
    }
}