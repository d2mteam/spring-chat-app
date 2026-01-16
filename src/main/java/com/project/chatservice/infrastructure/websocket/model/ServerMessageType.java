package com.project.chatservice.infrastructure.websocket.model;

/**
 * Represents the server message type.
 */
public enum ServerMessageType {
    CHAT_MESSAGE,
    READ_RECEIPT,
    NOTIFICATION,
    ERROR
}
