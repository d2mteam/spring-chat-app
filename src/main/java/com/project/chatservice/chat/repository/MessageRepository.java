package com.project.chatservice.chat.repository;

import com.project.chatservice.chat.domain.Message;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Represents the message repository.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByRoomIdOrderByCreatedAtAscIdAsc(Long roomId, Pageable pageable);

    List<Message> findByRoomIdAndIdGreaterThanOrderByCreatedAtAscIdAsc(Long roomId, Long afterId);

    List<Message> findByRoomIdAndParentIdOrderByCreatedAtAscIdAsc(Long roomId, Long parentId);
}
