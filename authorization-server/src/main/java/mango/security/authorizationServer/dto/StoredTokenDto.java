package mango.security.authorizationServer.dto;

import java.io.Serializable;
import java.time.Instant;

public record StoredTokenDto(
        String tokenValue,
        String principalName,
        String clientId,
        String tokenType, // "access" or "refresh"
        Instant issuedAt,
        Instant expiresAt
) implements Serializable {}
