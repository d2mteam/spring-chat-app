package com.project.chatservice.auth.infrastructure.persistence;

import com.project.chatservice.auth.domain.ChatUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Represents the chat user repository.
 */
public interface ChatUserRepository extends JpaRepository<ChatUser, UUID> {

    Optional<ChatUser> findByUsername(String username);
}
