package com.project.chatservice.security;

import com.project.chatservice.config.WebSocketProperties;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Component
public class UserHandshakeHandler extends DefaultHandshakeHandler {

    private final WebSocketProperties webSocketProperties;

    public UserHandshakeHandler(WebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        Object userId = attributes.get("userId");
        String name = userId != null
            ? userId.toString()
            : webSocketProperties.guestUserPrefix() + UUID.randomUUID();
        return () -> name;
    }
}
