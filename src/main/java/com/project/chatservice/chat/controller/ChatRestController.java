package com.project.chatservice.chat.controller;

import com.project.chatservice.chat.domain.ChatRoom;
import com.project.chatservice.chat.domain.Message;
import com.project.chatservice.chat.service.MessageService;
import com.project.chatservice.chat.service.RoomService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatRestController {

    private final MessageService messageService;
    private final RoomService roomService;
    private final ChatMessageMapper messageMapper;

    public ChatRestController(MessageService messageService,
                              RoomService roomService,
                              ChatMessageMapper messageMapper) {
        this.messageService = messageService;
        this.roomService = roomService;
        this.messageMapper = messageMapper;
    }

    @GetMapping("/api/rooms")
    public List<ChatRoom> listRooms() {
        return roomService.listRooms();
    }

    @PostMapping("/api/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatRoom createRoom(@Valid @RequestBody ChatRoomRequest request) {
        return roomService.createRoom(request.getName());
    }

    @GetMapping("/api/rooms/{roomId}/messages")
    public List<ChatMessageResponse> getMessages(@PathVariable Long roomId,
                                                 @RequestParam(required = false) Long afterId,
                                                 @RequestParam(defaultValue = "50") int limit) {
        List<Message> messages = afterId == null
            ? messageService.getRecentMessages(roomId, limit)
            : messageService.getMessagesAfter(roomId, afterId);
        return messages.stream()
            .map(messageMapper::toResponse)
            .collect(Collectors.toList());
    }

    @GetMapping("/api/rooms/{roomId}/messages/{messageId}/thread")
    public List<ChatMessageResponse> getThread(@PathVariable Long roomId,
                                               @PathVariable Long messageId) {
        return messageService.getThreadMessages(roomId, messageId).stream()
            .map(messageMapper::toResponse)
            .collect(Collectors.toList());
    }

    @PatchMapping("/api/rooms/{roomId}/messages/{messageId}")
    public ResponseEntity<ChatMessageResponse> editMessage(@PathVariable Long roomId,
                                                           @PathVariable Long messageId,
                                                           @Valid @RequestBody ChatMessageEditRequest request) {
        Message message = messageService.editMessage(roomId, messageId, request.getEditorId(), request.getContent());
        return ResponseEntity.ok(messageMapper.toResponse(message));
    }

    @DeleteMapping("/api/rooms/{roomId}/messages/{messageId}")
    public ResponseEntity<ChatMessageResponse> deleteMessage(@PathVariable Long roomId,
                                                             @PathVariable Long messageId,
                                                             @RequestParam String deleterId) {
        Message message = messageService.deleteMessage(roomId, messageId, deleterId);
        return ResponseEntity.ok(messageMapper.toResponse(message));
    }

    @PostMapping("/api/rooms/{roomId}/messages/{messageId}/read")
    public ResponseEntity<Void> markRead(@PathVariable Long roomId,
                                         @PathVariable Long messageId,
                                         @RequestParam String userId) {
        messageService.markRead(roomId, messageId, userId);
        return ResponseEntity.accepted().build();
    }

}
