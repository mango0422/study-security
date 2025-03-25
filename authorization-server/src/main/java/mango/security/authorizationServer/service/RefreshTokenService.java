package mango.security.authorizationServer.service;

import mango.security.authorizationServer.dto.StoredTokenDto;

public interface RefreshTokenService {
    StoredTokenDto getRefreshToken(String refreshTokenValue);
}
