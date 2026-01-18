package com.project.chatservice.infrastructure.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Represents the chat web socket handler.
 *
 * <p>Luồng chuẩn:
 * <ul>
 *   <li>HTTP Login: Client login, server trả JWT.</li>
 *   <li>WebSocket Connect (1 lần): Client mở WS + gửi JWT (handshake header/query hoặc AUTH message đầu tiên).
 *       Server verify JWT, OK thì đánh dấu connection đã auth + trả sessionId.</li>
 *   <li>Gửi tin nhắn (nhiều lần): Client gửi message bình thường không cần JWT. Server lấy user từ
 *       connection/session context.</li>
 *   <li>sessionId chỉ để tracking/debug/ack, không phải token xác thực.</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketMessageRouter messageRouter;
    private final SessionRegistry sessionRegistry;
    private final SessionSender sessionSender;
    private final WebSocketUserResolver userResolver;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = userResolver.resolveUserId(session);
        sessionSender.register(session);
        sessionRegistry.register(session.getId(), userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        messageRouter.route(session.getId(), message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionSender.remove(session.getId());
        sessionRegistry.remove(session.getId());
    }

}
