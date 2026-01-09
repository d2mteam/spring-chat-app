package com.project.chatservice.infrastructure.websocket.model;

/**
 * Represents the server message type.
 */
public enum ServerMessageType {
    MESSAGE,
    RECEIPT,
    NOTIFICATION,
    ACK,
    ERROR
}
