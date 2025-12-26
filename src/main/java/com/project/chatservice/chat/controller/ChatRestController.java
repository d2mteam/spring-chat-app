package com.project.chatservice.chat.controller;

import com.project.chatservice.chat.domain.ChatRoom;
import com.project.chatservice.chat.domain.Message;
import com.project.chatservice.chat.service.MessageService;
import com.project.chatservice.chat.service.RoomService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
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

    public ChatRestController(MessageService messageService, RoomService roomService) {
        this.messageService = messageService;
        this.roomService = roomService;
    }

    @GetMapping("/api/rooms")
    public List<ChatRoom> listRooms() {
        return roomService.listRooms();
    }

    @PostMapping("/api/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatRoom createRoom(@RequestBody ChatRoomRequest request) {
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
            .map(message -> new ChatMessageResponse(
                message.getId(),
                message.getRoom().getId(),
                message.getSenderId(),
                message.getContent(),
                message.getCreatedAt()
            ))
            .collect(Collectors.toList());
    }
}
