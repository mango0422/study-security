package mango.security.authorizationServer.service;

import mango.security.authorizationServer.dto.StoredTokenDto;
import mango.security.authorizationServer.dto.TokenPairDto;
import org.springframework.stereotype.Service;

@Service
public class TokenPersistenceService {
    private final TokenRedisService tokenRedisService;

    public TokenPersistenceService(TokenRedisService tokenRedisService) {
        this.tokenRedisService = tokenRedisService;
    }

    public void persistTokenPair(StoredTokenDto accessToken, StoredTokenDto refreshToken) {
        if (refreshToken != null) {
            TokenPairDto pair = new TokenPairDto(accessToken, refreshToken);
            tokenRedisService.saveTokenPair(pair);
        } else {
            // refresh token이 없는 경우 단일 저장
            tokenRedisService.saveToken(accessToken);
        }
    }
}
