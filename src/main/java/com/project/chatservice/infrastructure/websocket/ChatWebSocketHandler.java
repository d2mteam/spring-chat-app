package com.project.chatservice.infrastructure.websocket;

import com.project.chatservice.infrastructure.websocket.model.AuthResponse;
import com.project.chatservice.infrastructure.websocket.model.ServerMessageEnvelope;
import com.project.chatservice.infrastructure.websocket.model.ServerMessageType;
import com.project.chatservice.config.WebSocketProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Represents the chat web socket handler.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketMessageRouter messageRouter;
    private final SessionRegistry sessionRegistry;
    private final SessionSender sessionSender;
    private final WebSocketUserResolver userResolver;
    private final WebSocketMessageSender messageSender;
    private final WebSocketProperties webSocketProperties;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, Instant> lastPongTimes = new ConcurrentHashMap<>();
    private final ScheduledExecutorService heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionSender.register(session);
        sessions.put(session.getId(), session);
        lastPongTimes.put(session.getId(), Instant.now());
        if (isAuthenticated(session)) {
            String userId = userResolver.resolveUserId(session);
            sessionRegistry.register(session.getId(), userId);
            sendAuthSuccess(session.getId());
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        messageRouter.route(session.getId(), message.getPayload());
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) {
        lastPongTimes.put(session.getId(), Instant.now());
    }

    @Override
    protected void handlePingMessage(WebSocketSession session, PingMessage message) throws IOException {
        ByteBuffer payload = message.getPayload() != null ? message.getPayload() : ByteBuffer.allocate(0);
        session.sendMessage(new PongMessage(payload));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionSender.remove(session.getId());
        sessionRegistry.remove(session.getId());
        sessions.remove(session.getId());
        lastPongTimes.remove(session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        sessions.remove(session.getId());
        lastPongTimes.remove(session.getId());
    }

    @PostConstruct
    public void startHeartbeat() {
        WebSocketProperties.HeartbeatProperties heartbeat = webSocketProperties.heartbeat();
        if (heartbeat == null) {
            return;
        }
        Duration pingInterval = heartbeat.pingInterval();
        Duration pongTimeout = heartbeat.pongTimeout();
        if (pingInterval == null || pongTimeout == null) {
            return;
        }
        heartbeatExecutor.scheduleAtFixedRate(
            () -> sendHeartbeat(pongTimeout),
            pingInterval.toMillis(),
            pingInterval.toMillis(),
            TimeUnit.MILLISECONDS
        );
    }

    @PreDestroy
    public void stopHeartbeat() {
        heartbeatExecutor.shutdownNow();
    }

    private boolean isAuthenticated(WebSocketSession session) {
        Object authenticated = session.getAttributes().get("authenticated");
        return authenticated instanceof Boolean && (Boolean) authenticated;
    }

    private void sendAuthSuccess(String sessionId) {
        AuthResponse payload = new AuthResponse(sessionId);
        ServerMessageEnvelope envelope = new ServerMessageEnvelope(
            1,
            ServerMessageType.AUTH,
            sessionId,
            System.currentTimeMillis(),
            payload
        );
        messageSender.sendToSession(sessionId, envelope);
    }

    private void sendHeartbeat(Duration pongTimeout) {
        Instant now = Instant.now();
        for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            String sessionId = entry.getKey();
            WebSocketSession session = entry.getValue();
            if (session == null || !session.isOpen()) {
                sessions.remove(sessionId);
                lastPongTimes.remove(sessionId);
                continue;
            }
            Instant lastPong = lastPongTimes.getOrDefault(sessionId, now);
            if (now.minus(pongTimeout).isAfter(lastPong)) {
                closeSession(session);
                sessions.remove(sessionId);
                lastPongTimes.remove(sessionId);
                continue;
            }
            try {
                session.sendMessage(new PingMessage());
            } catch (IOException ex) {
                closeSession(session);
                sessions.remove(sessionId);
                lastPongTimes.remove(sessionId);
            }
        }
    }

    private void closeSession(WebSocketSession session) {
        try {
            session.close(CloseStatus.SESSION_NOT_RELIABLE);
        } catch (IOException ex) {
            log.debug("Failed to close websocket session {}", session.getId(), ex);
        }
    }
}
