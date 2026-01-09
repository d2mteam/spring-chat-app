package com.project.chatservice.chat.service;

import com.project.chatservice.chat.domain.ChatRoom;
import com.project.chatservice.chat.repository.RoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Represents the room service.
 */
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    @Transactional
    public ChatRoom createRoom(String name) {
        return roomRepository.save(ChatRoom.builder()
                .name(name)
                .build());
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> listRooms() {
        return roomRepository.findAll();
    }
}
