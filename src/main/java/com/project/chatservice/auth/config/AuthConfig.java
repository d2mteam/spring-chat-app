package com.project.chatservice.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatservice.auth.application.port.AuthSessionStore;
import com.project.chatservice.auth.infrastructure.session.InMemoryAuthSessionStore;
import com.project.chatservice.auth.infrastructure.session.RedisAuthSessionStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Represents auth configuration.
 */
@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(name = "chat.auth.session-store", havingValue = "redis", matchIfMissing = true)
    public AuthSessionStore redisAuthSessionStore(RedisTemplate<String, String> redisTemplate,
                                                  ObjectMapper objectMapper,
                                                  AuthProperties properties) {
        return new RedisAuthSessionStore(redisTemplate, objectMapper, properties);
    }

    @Bean
    @ConditionalOnProperty(name = "chat.auth.session-store", havingValue = "memory")
    public AuthSessionStore inMemoryAuthSessionStore() {
        return new InMemoryAuthSessionStore();
    }
}
