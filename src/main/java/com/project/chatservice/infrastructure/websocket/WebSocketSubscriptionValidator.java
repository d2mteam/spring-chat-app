package com.project.chatservice.infrastructure.websocket;

import com.project.chatservice.config.WebSocketProperties;
import org.springframework.stereotype.Component;

@Component
public class WebSocketSubscriptionValidator {

    private final WebSocketProperties.DestinationProperties destinationProperties;

    public WebSocketSubscriptionValidator(WebSocketProperties properties) {
        this.destinationProperties = properties.destinations();
    }

    public boolean isAllowed(String destination, String userId) {
        if (destination == null || destination.isBlank()) {
            return false;
        }
        String roomsPrefix = destinationProperties.rooms() + "/";
        if (destination.startsWith(roomsPrefix)) {
            return true;
        }
        String usersPrefix = destinationProperties.users() + "/";
        if (destination.startsWith(usersPrefix)) {
            String expected = usersPrefix + userId + "/" + destinationProperties.notificationsSuffix();
            return destination.equals(expected);
        }
        return false;
    }
}
