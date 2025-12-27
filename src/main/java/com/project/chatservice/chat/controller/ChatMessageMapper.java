package com.project.chatservice.chat.controller;

import com.project.chatservice.chat.domain.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    @Mapping(target = "roomId", source = "room.id")
    ChatMessageResponse toResponse(Message message);
}
