package com.project.chatservice.infrastructure.websocket.model;

public enum ClientMessageType {
    SUBSCRIBE,
    UNSUBSCRIBE,
    SEND_MESSAGE,
    MARK_READ,
    ACK
}
