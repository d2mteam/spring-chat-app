package com.project.chatservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatservice.infrastructure.redis.RedisNotificationPublisher;
import com.project.chatservice.infrastructure.redis.RedisNotificationSubscriber;
import com.project.chatservice.infrastructure.redis.RedisPublisher;
import com.project.chatservice.infrastructure.redis.RedisReceiptPublisher;
import com.project.chatservice.infrastructure.redis.RedisReceiptSubscriber;
import com.project.chatservice.infrastructure.redis.RedisSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       RedisSubscriber subscriber,
                                                                       RedisReceiptSubscriber receiptSubscriber,
                                                                       RedisNotificationSubscriber notificationSubscriber) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(subscriber, new PatternTopic(RedisPublisher.CHANNEL));
        container.addMessageListener(receiptSubscriber, new PatternTopic(RedisReceiptPublisher.CHANNEL));
        container.addMessageListener(notificationSubscriber, new PatternTopic(RedisNotificationPublisher.CHANNEL));
        return container;
    }
}
