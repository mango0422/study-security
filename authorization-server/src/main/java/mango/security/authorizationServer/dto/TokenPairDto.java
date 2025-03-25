package mango.security.authorizationServer.dto;

import java.io.Serializable;

public record TokenPairDto(
        StoredTokenDto accessToken,
        StoredTokenDto refreshToken
) implements Serializable {}
