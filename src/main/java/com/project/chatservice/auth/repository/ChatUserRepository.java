package com.project.chatservice.auth.repository;

import com.project.chatservice.auth.model.ChatUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Represents the chat user repository.
 */
public interface ChatUserRepository extends JpaRepository<ChatUser, UUID> {

    Optional<ChatUser> findByUsername(String username);
}
