package com.project.chatservice.infrastructure.websocket.model;

/**
 * Represents the client message type.
 */
public enum ClientMessageType {
    AUTH,
    SUBSCRIBE,
    UNSUBSCRIBE,
    SEND_MESSAGE,
    MARK_READ,
    ACK
}
