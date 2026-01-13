package com.project.chatservice.chat.service;

import com.project.chatservice.chat.domain.ChatRoom;
import com.project.chatservice.chat.domain.Message;
import com.project.chatservice.chat.domain.MessageReceipt;
import com.project.chatservice.chat.repository.MessageReceiptRepository;
import com.project.chatservice.chat.repository.MessageRepository;
import com.project.chatservice.chat.repository.RoomRepository;
import com.project.chatservice.infrastructure.redis.RedisNotificationPublisher;
import com.project.chatservice.infrastructure.redis.RedisPublisher;
import com.project.chatservice.infrastructure.redis.RedisReceiptPublisher;
import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Represents the message service.
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    // Task 8: regex tìm mention theo dạng @username.
    private static final Pattern MENTION_PATTERN = Pattern.compile("@([A-Za-z0-9_.-]+)");

    private final MessageRepository messageRepository;
    private final MessageReceiptRepository messageReceiptRepository;
    private final RoomRepository roomRepository;
    private final RedisPublisher redisPublisher;
    private final RedisReceiptPublisher receiptPublisher;
    private final RedisNotificationPublisher notificationPublisher;

    @Transactional
    public ChatMessageEvent saveAndPublish(Long roomId,
                                           String senderId,
                                           String content,
                                           Long parentId,
                                           String contentType,
                                           String attachmentUrl,
                                           String attachmentName,
                                           String attachmentMimeType) {
        // Task 2 & 6: lưu message có thread và thông tin đính kèm.
        ChatRoom room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        Message message = messageRepository.save(new Message(
            room,
            senderId,
            content,
            parentId,
            contentType,
            attachmentUrl,
            attachmentName,
            attachmentMimeType
        ));
        ChatMessageEvent event = toEvent(message);
        redisPublisher.publish(event);
        publishMentions(message);
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

    @Transactional(readOnly = true)
    public List<Message> getThreadMessages(Long roomId, Long parentId) {
        // Task 2: lấy message theo parentId để hiển thị thread.
        return messageRepository.findByRoomIdAndParentIdOrderByCreatedAtAscIdAsc(roomId, parentId);
    }

    @Transactional
    public Message editMessage(Long roomId, Long messageId, String editorId, String newContent) {
        // Task 5: chỉnh sửa nội dung message và broadcast lại.
        Message message = getMessageInRoom(roomId, messageId);
        if (message.getDeletedAt() != null) {
            throw new IllegalStateException("Cannot edit deleted message");
        }
        message.editContent(newContent);
        ChatMessageEvent event = toEvent(message);
        redisPublisher.publish(event);
        return message;
    }

    @Transactional
    public Message deleteMessage(Long roomId, Long messageId, String deleterId) {
        // Task 5: xóa mềm message để giữ audit trail.
        Message message = getMessageInRoom(roomId, messageId);
        message.softDelete(deleterId);
        ChatMessageEvent event = toEvent(message);
        redisPublisher.publish(event);
        return message;
    }

    @Transactional
    public ReceiptEvent markRead(Long roomId, Long messageId, String userId) {
        // Task 1: ghi lại trạng thái đã đọc theo user.
        Message message = getMessageInRoom(roomId, messageId);
        MessageReceipt receipt = messageReceiptRepository.findByMessageIdAndUserId(messageId, userId)
            .orElseGet(() -> new MessageReceipt(message, userId, "READ"));
        receipt.markStatus("READ");
        messageReceiptRepository.save(receipt);
        ReceiptEvent event = new ReceiptEvent(roomId, messageId, userId, receipt.getStatus(), receipt.getUpdatedAt());
        receiptPublisher.publish(event);
        return event;
    }

    private Message getMessageInRoom(Long roomId, Long messageId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        if (!message.getRoom().getId().equals(roomId)) {
            throw new IllegalArgumentException("Message not in room");
        }
        return message;
    }

    private ChatMessageEvent toEvent(Message message) {
        return new ChatMessageEvent(
            message.getRoom().getId(),
            message.getId(),
            message.getSenderId(),
            message.getContent(),
            message.getParentId(),
            message.getContentType(),
            message.getAttachmentUrl(),
            message.getAttachmentName(),
            message.getAttachmentMimeType(),
            message.getCreatedAt(),
            message.getEditedAt(),
            message.getDeletedAt(),
            message.getDeletedBy()
        );
    }

    private void publishMentions(Message message) {
        // Task 8: phát hiện mention và gửi notification realtime.
        String content = message.getContent();
        if (content == null) {
            return;
        }
        Matcher matcher = MENTION_PATTERN.matcher(content);
        while (matcher.find()) {
            String mentionedUser = matcher.group(1);
            NotificationEvent event = new NotificationEvent(
                mentionedUser,
                message.getRoom().getId(),
                message.getId(),
                "MENTION",
                buildPreview(message),
                Instant.now()
            );
            notificationPublisher.publish(event);
        }
    }

    private String buildPreview(Message message) {
        String content = message.getContent();
        if (content == null) {
            return "";
        }
        return content.length() > 120 ? content.substring(0, 120) : content;
    }
}
