package com.project.chatservice.chat.service;

import com.project.chatservice.chat.domain.ChatRoom;
import com.project.chatservice.chat.domain.Message;
import com.project.chatservice.chat.repository.MessageRepository;
import com.project.chatservice.chat.repository.RoomRepository;
import com.project.chatservice.infrastructure.redis.RedisPublisher;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final RedisPublisher redisPublisher;

    public MessageService(MessageRepository messageRepository,
                          RoomRepository roomRepository,
                          RedisPublisher redisPublisher) {
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.redisPublisher = redisPublisher;
    }

    @Transactional
    public ChatMessageEvent saveAndPublish(Long roomId, String senderId, String content) {
        ChatRoom room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        Message message = messageRepository.save(new Message(room, senderId, content));
        ChatMessageEvent event = new ChatMessageEvent(
            roomId,
            message.getId(),
            senderId,
            content,
            message.getCreatedAt()
        );
        redisPublisher.publish(event);
        return event;
    }

    @Transactional(readOnly = true)
    public List<Message> getRecentMessages(Long roomId, int limit) {
        return messageRepository.findByRoomIdOrderByCreatedAtAscIdAsc(roomId, PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public List<Message> getMessagesAfter(Long roomId, Long afterId) {
        return messageRepository.findByRoomIdAndIdGreaterThanOrderByCreatedAtAscIdAsc(roomId, afterId);
    }
}
