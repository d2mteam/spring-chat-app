package com.project.chatservice.auth.jwt;

import com.project.chatservice.auth.config.AuthProperties;
import com.project.chatservice.auth.session.AuthSession;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

/**
 * Represents the JWT service.
 */
@Component
public class JwtService {

    private final AuthProperties properties;
    private final SecretKey signingKey;

    public JwtService(AuthProperties properties) {
        this.properties = properties;
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.jwt().secret()));
    }

    public String generateToken(AuthSession session) {
        return Jwts.builder()
            .issuer(properties.jwt().issuer())
            .subject(session.userId())
            .claim("username", session.username())
            .claim("tokenId", session.tokenId())
            .issuedAt(Date.from(session.issuedAt()))
            .expiration(Date.from(session.expiresAt()))
            .signWith(signingKey)
            .compact();
    }

    public JwtClaims parseToken(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
        String userId = claims.getSubject();
        String username = claims.get("username", String.class);
        String tokenId = claims.get("tokenId", String.class);
        Instant expiresAt = claims.getExpiration().toInstant();
        return new JwtClaims(userId, username, tokenId, expiresAt);
    }
}
