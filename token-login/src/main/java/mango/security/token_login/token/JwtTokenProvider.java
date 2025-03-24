package mango.security.token_login.token;

import mango.security.token_login.service.RedisKeyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import java.security.Key;

@Component
public class JwtTokenProvider extends AbstractJwtTokenProvider {

    private final RedisKeyService redisKeyService;

    public JwtTokenProvider(@Value("${jwt.accessToken.ttl}") long expireTime,
                            RedisKeyService redisKeyService) {
        this.expireTime = expireTime;
        this.redisKeyService = redisKeyService;
    }

    @Override
    protected Key getKey() {
        String secret = redisKeyService.getCurrentSecretKey();
        return hmacShaKeyFor(secret.getBytes());
    }
}
