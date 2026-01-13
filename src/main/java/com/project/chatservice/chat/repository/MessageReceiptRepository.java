package com.project.chatservice.chat.repository;

import com.project.chatservice.chat.domain.MessageReceipt;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Represents the message receipt repository.
 */
public interface MessageReceiptRepository extends JpaRepository<MessageReceipt, Long> {

    Optional<MessageReceipt> findByMessageIdAndUserId(Long messageId, String userId);
}
