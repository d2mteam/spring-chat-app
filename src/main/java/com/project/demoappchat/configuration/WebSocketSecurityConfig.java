package com.project.demoappchat.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity // 1. Bắt buộc phải có annotation này
public class WebSocketSecurityConfig {

    @Bean
    // 2. Định nghĩa AuthorizationManager thay vì override configureInbound
    public AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        return messages
                // Các tin nhắn hệ thống (CONNECT, DISCONNECT...) -> Cho qua
                // Lưu ý: Token vẫn bị check ở tầng Interceptor trước đó, ở đây chỉ check quyền truy cập
                .nullDestMatcher().permitAll()

                // Phân quyền SUBSCRIBE (Chiều Server -> Client)
                .simpSubscribeDestMatchers("/topic/admin/**").hasRole("ADMIN")
                .simpSubscribeDestMatchers("/topic/**").authenticated()

                // Phân quyền SEND (Chiều Client -> Server)
                .simpDestMatchers("/app/**").authenticated()

                // Còn lại chặn hết
                .anyMessage().denyAll()
                .build();
    }
}