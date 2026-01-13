package com.project.chatservice.chat.repository;

import com.project.chatservice.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Represents the room repository.
 */
public interface RoomRepository extends JpaRepository<ChatRoom, Long> {
}
