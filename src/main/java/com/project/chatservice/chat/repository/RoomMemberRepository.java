package com.project.chatservice.chat.repository;

import com.project.chatservice.chat.domain.RoomMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Represents the room member repository.
 */
public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {

    List<RoomMember> findByRoomId(Long roomId);
}
