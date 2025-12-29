package com.project.chatservice.infrastructure.websocket.handler;

import com.project.chatservice.infrastructure.websocket.model.ClientMessageType;
import org.springframework.web.socket.WebSocketSession;

public interface ClientMessageHandler {

    ClientMessageType supports();

    Class<?> payloadType();

    void handle(WebSocketSession session, Object payload);
}
