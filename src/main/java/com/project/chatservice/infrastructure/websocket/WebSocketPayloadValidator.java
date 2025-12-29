package com.project.chatservice.infrastructure.websocket;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class WebSocketPayloadValidator {

    private final Validator validator;

    public WebSocketPayloadValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Payload is required");
        }
        Set<ConstraintViolation<T>> violations = validator.validate(payload);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Invalid payload: " + message);
        }
    }
}
