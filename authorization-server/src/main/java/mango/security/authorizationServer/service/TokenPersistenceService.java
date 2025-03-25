package mango.security.authorizationServer.service;

import mango.security.authorizationServer.dto.StoredTokenDto;
import mango.security.authorizationServer.dto.TokenPairDto;

public class TokenPersistenceService {
    private final TokenRedisService tokenRedisService;

    public TokenPersistenceService(TokenRedisService tokenRedisService) {
        this.tokenRedisService = tokenRedisService;
    }

    public void persisTokenPair(StoredTokenDto accessToken, StoredTokenDto refreshToken) {
        TokenPairDto pair = new TokenPairDto(accessToken, refreshToken);
        tokenRedisService.saveTokenPair(pair);
    }
}
