package mango.security.token_login.token;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mango.security.token_login.token.RefreshTokenProvider.RefreshTokenStore;

@Component
@Qualifier("jpa")
@RequiredArgsConstructor
public class JpaRefreshTokenStore implements RefreshTokenStore {
    private final StringRedisTemplate redisTemplate;

    @Override public RefreshTokenStrategy strategy() {
        return RefreshTokenStrategy.REDIS;
    }

    @Override public void save(String accessToken, String refreshToken, long ttl) {
        redisTemplate.opsForValue().set(accessToken, refreshToken, ttl);
    }

    @Override
    public boolean validate(String accessToken, String refreshToken) {
        String storedRefreshToken = redisTemplate.opsForValue().get(accessToken);
        return storedRefreshToken != null && storedRefreshToken.equals(refreshToken);
    }

    @Override
    public void remove(String accessToken) {
        redisTemplate.delete(accessToken);
    }
}
