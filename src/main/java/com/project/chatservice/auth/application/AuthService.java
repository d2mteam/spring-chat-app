package com.project.chatservice.auth.application;

import com.project.chatservice.auth.config.AuthProperties;
import com.project.chatservice.auth.domain.AuthSession;
import com.project.chatservice.auth.domain.ChatUser;
import com.project.chatservice.auth.infrastructure.jwt.JwtService;
import com.project.chatservice.auth.infrastructure.persistence.ChatUserRepository;
import com.project.chatservice.auth.application.port.AuthSessionStore;
import com.project.chatservice.auth.interfaces.web.dto.AuthResponse;
import com.project.chatservice.auth.interfaces.web.dto.LoginRequest;
import com.project.chatservice.auth.interfaces.web.dto.RegisterRequest;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Represents the auth service.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final ChatUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthSessionStore sessionStore;
    private final AuthProperties properties;

    public AuthResponse register(RegisterRequest request) {
        String username = request.username().trim();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        String hashedPassword = passwordEncoder.encode(request.password());
        ChatUser user = new ChatUser(username, hashedPassword, Instant.now());
        ChatUser savedUser = userRepository.save(user);
        return issueToken(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        ChatUser user = userRepository.findByUsername(request.username().trim())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return issueToken(user);
    }

    private AuthResponse issueToken(ChatUser user) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(properties.jwt().accessTokenTtl());
        String tokenId = UUID.randomUUID().toString();
        AuthSession session = new AuthSession(tokenId, user.getId().toString(), user.getUsername(), issuedAt, expiresAt);
        sessionStore.store(session);
        String token = jwtService.generateToken(session);
        return new AuthResponse(token, "Bearer", expiresAt, user.getId().toString(), user.getUsername());
    }
}
