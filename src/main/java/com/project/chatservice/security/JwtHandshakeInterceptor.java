package com.project.chatservice.security;

import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * Represents the jwt handshake interceptor.
 */
@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {
        String token = resolveToken(request);
        if (StringUtils.hasText(token)) {
            attributes.put("userId", token);
            attributes.put("authenticated", true);
            return true;
        }
        List<String> authHeaders = request.getHeaders().get("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String header = authHeaders.get(0);
            if (header.startsWith("Bearer ")) {
                String userId = header.substring("Bearer ".length());
                attributes.put("userId", userId);
                attributes.put("authenticated", true);
                return true;
            }
        }
        List<String> userHeaders = request.getHeaders().get("X-User-Id");
        if (userHeaders != null && !userHeaders.isEmpty()) {
            attributes.put("userId", userHeaders.get(0));
            attributes.put("authenticated", true);
        }
        attributes.putIfAbsent("authenticated", false);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
    }

    private String resolveToken(ServerHttpRequest request) {
        return UriComponentsBuilder.fromUri(request.getURI())
            .build()
            .getQueryParams()
            .toSingleValueMap()
            .entrySet()
            .stream()
            .filter(entry -> "token".equals(entry.getKey()) || "jwt".equals(entry.getKey()))
            .map(Map.Entry::getValue)
            .filter(StringUtils::hasText)
            .findFirst()
            .map(this::stripBearerPrefix)
            .orElse(null);
    }

    private String stripBearerPrefix(String token) {
        if (token.startsWith("Bearer ")) {
            return token.substring("Bearer ".length());
        }
        return token;
    }
}
