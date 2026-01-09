package com.project.chatservice.infrastructure.websocket;

import com.project.chatservice.config.WebSocketProperties;
import org.springframework.stereotype.Component;

/**
 * Represents the web socket destination resolver.
 */
@Component
public class WebSocketDestinationResolver {

    private final WebSocketProperties.DestinationProperties destinationProperties;

    public WebSocketDestinationResolver(WebSocketProperties properties) {
        this.destinationProperties = properties.destinations();
    }

    public String roomMessages(Long roomId) {
        return destinationProperties.rooms() + "/" + roomId;
    }

    public String roomReceipts(Long roomId) {
        return destinationProperties.rooms() + "/" + roomId + "/" + destinationProperties.receiptsSuffix();
    }

    public String userNotifications(String userId) {
        return destinationProperties.users() + "/" + userId + "/" + destinationProperties.notificationsSuffix();
    }
}
