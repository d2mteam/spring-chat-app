package com.project.chatservice.infrastructure.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatservice.infrastructure.websocket.handler.ClientMessageHandler;
import com.project.chatservice.infrastructure.websocket.model.ClientMessageEnvelope;
import com.project.chatservice.infrastructure.websocket.model.ClientMessageType;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * Represents the web socket message router.
 */
@Component
@Slf4j
public class WebSocketMessageRouter {

    private final ObjectMapper objectMapper;
    private final Map<ClientMessageType, ClientMessageHandler> handlers;

    public WebSocketMessageRouter(ObjectMapper objectMapper, List<ClientMessageHandler> handlers) {
        this.objectMapper = objectMapper;
        this.handlers = new EnumMap<>(ClientMessageType.class);
        handlers.forEach(handler -> this.handlers.put(handler.supports(), handler));
    }

    public void route(WebSocketSession session, ClientMessageEnvelope envelope) {
        if (envelope == null || envelope.type() == null) {
            log.debug("Ignoring websocket message with missing type from session {}", session.getId());
            return;
        }
        ClientMessageHandler handler = handlers.get(envelope.type());
        if (handler == null) {
            log.debug("Ignoring websocket message with unsupported type {} from session {}", envelope.type(),
                session.getId());
            return;
        }
        Object payload = objectMapper.convertValue(envelope.payload(), handler.payloadType());
        if (payload == null) {
            throw new IllegalArgumentException("Payload is required for message type: " + envelope.type());
        }
        handler.handle(session, payload);
    }
}
