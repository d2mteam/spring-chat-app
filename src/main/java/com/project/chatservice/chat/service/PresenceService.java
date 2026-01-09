package com.project.chatservice.chat.service;

import java.time.Duration;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Represents the presence service.
 */
@Service
@RequiredArgsConstructor
public class PresenceService {

    private static final Duration PRESENCE_TTL = Duration.ofMinutes(30);

    private final RedisTemplate<String, String> redisTemplate;

    public void markOnline(Long roomId, String userId) {
        String key = presenceKey(roomId);
        redisTemplate.opsForSet().add(key, userId);
        redisTemplate.expire(key, PRESENCE_TTL);
    }

    public void markOffline(Long roomId, String userId) {
        String key = presenceKey(roomId);
        redisTemplate.opsForSet().remove(key, userId);
    }

    public Set<String> listOnline(Long roomId) {
        String key = presenceKey(roomId);
        return redisTemplate.opsForSet().members(key);
    }

    private String presenceKey(Long roomId) {
        return "presence:room:" + roomId;
    }
}
