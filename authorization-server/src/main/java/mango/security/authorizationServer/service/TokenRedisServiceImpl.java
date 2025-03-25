package mango.security.authorizationServer.service;

import mango.security.authorizationServer.dto.StoredTokenDto;
import mango.security.authorizationServer.dto.TokenPairDto;
import mango.security.authorizationServer.exception.AuthException;
import mango.security.authorizationServer.type.ApiResponseType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenRedisServiceImpl implements TokenRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public TokenRedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String buildKey(String tokenType, String tokenValue) {
        return tokenType + "_token:" + tokenValue;
    }

    @Override
    public void saveToken(StoredTokenDto token) {
        String key = buildKey(token.tokenType(), token.tokenValue());
        Duration ttl = Duration.between(token.issuedAt(), token.expiresAt());
        redisTemplate.opsForValue().set(key, token, ttl);
    }

    @Override
    public StoredTokenDto getToken(String tokenValue, String tokenType) {
        String key = buildKey(tokenType, tokenValue);
        Object value = redisTemplate.opsForValue().get(key);
        if (value instanceof StoredTokenDto dto) {
            return dto;
        }
        throw new AuthException(ApiResponseType.INVALID_TOKEN);
    }

    @Override
    public void deleteToken(String tokenValue, String tokenType) {
        redisTemplate.delete(buildKey(tokenType, tokenValue));
    }

    @Override
    public void saveTokenPair(TokenPairDto pair) {
        saveToken(pair.accessToken());
        saveToken(pair.refreshToken());

        // token 연관성 저장
        redisTemplate.opsForValue().set("access_to_refresh:" + pair.accessToken().tokenValue(), pair.refreshToken().tokenValue());
        redisTemplate.opsForValue().set("refresh_to_access:" + pair.refreshToken().tokenValue(), pair.accessToken().tokenValue());
    }

    @Override
    public void deleteTokenPair(String accessTokenValue, String refreshTokenValue) {
        deleteToken(accessTokenValue, "access");
        deleteToken(refreshTokenValue, "refresh");

        redisTemplate.delete("access_to_refresh:" + accessTokenValue);
        redisTemplate.delete("refresh_to_access:" + refreshTokenValue);
    }
}
