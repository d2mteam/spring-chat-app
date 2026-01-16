package com.project.chatservice.infrastructure.websocket.handler;

import com.project.chatservice.infrastructure.websocket.SessionContext;
import com.project.chatservice.infrastructure.websocket.model.ClientMessageType;

/**
 * Represents the client message handler.
 */
public interface ClientMessageHandler {

    ClientMessageType supports();

    Class<?> payloadType();

    void handle(SessionContext context, Object payload);
}
