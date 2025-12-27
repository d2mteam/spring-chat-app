package com.project.chatservice.security;

import java.security.Principal;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    public String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            return authentication.getName();
        }
        return "anonymous";
    }

    public String getUserId(SimpMessageHeaderAccessor accessor) {
        Principal principal = accessor.getUser();
        if (principal != null) {
            return principal.getName();
        }
        return getUserId();
    }
}
