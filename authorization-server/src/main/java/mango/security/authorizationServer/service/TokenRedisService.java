package mango.security.authorizationServer.service;

import mango.security.authorizationServer.dto.StoredTokenDto;
import mango.security.authorizationServer.dto.TokenPairDto;

public interface TokenRedisService {
    void saveToken(StoredTokenDto token);
    StoredTokenDto getToken(String tokenValue, String tokenType);
    void deleteToken(String tokenValue, String tokenType);

    void saveTokenPair(TokenPairDto pair);
    void deleteTokenPair(String accessTokenValue, String refreshTokenValue);
}
