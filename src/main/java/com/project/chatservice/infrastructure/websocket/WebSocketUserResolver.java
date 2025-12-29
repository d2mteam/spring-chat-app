package com.project.chatservice.infrastructure.websocket;

import com.project.chatservice.config.WebSocketProperties;
import java.security.Principal;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WebSocketUserResolver {

    private final WebSocketProperties webSocketProperties;

    public WebSocketUserResolver(WebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
    }

    public String resolveUserId(WebSocketSession session) {
        Principal principal = session.getPrincipal();
        if (principal != null && principal.getName() != null && !principal.getName().isBlank()) {
            return principal.getName();
        }
        String anonymous = webSocketProperties.anonymousUserId();
        if (anonymous != null && !anonymous.isBlank()) {
            return anonymous;
        }
        return webSocketProperties.guestUserPrefix() + UUID.randomUUID();
    }
}
