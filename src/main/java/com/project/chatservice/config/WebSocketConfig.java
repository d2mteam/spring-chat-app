package com.project.chatservice.config;

import com.project.chatservice.security.JwtHandshakeInterceptor;
import com.project.chatservice.security.UserHandshakeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Represents the web socket config.
 */
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;
    private final UserHandshakeHandler userHandshakeHandler;
    private final WebSocketProperties webSocketProperties;
    private final org.springframework.web.socket.WebSocketHandler webSocketHandler;

    public WebSocketConfig(JwtHandshakeInterceptor jwtHandshakeInterceptor,
                           UserHandshakeHandler userHandshakeHandler,
                           WebSocketProperties webSocketProperties,
                           org.springframework.web.socket.WebSocketHandler webSocketHandler) {
        this.jwtHandshakeInterceptor = jwtHandshakeInterceptor;
        this.userHandshakeHandler = userHandshakeHandler;
        this.webSocketProperties = webSocketProperties;
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, webSocketProperties.endpoint())
            .setHandshakeHandler(userHandshakeHandler)
            .addInterceptors(jwtHandshakeInterceptor)
            .setAllowedOriginPatterns(webSocketProperties.allowedOrigins().toArray(String[]::new));
    }
}
