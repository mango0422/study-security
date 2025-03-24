package mango.security.token_login.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import mango.security.token_login.token.CustomUUID;

@Service
public class RedisKeyService {

    private final StringRedisTemplate redisTemplate;
    private static final String SECRET_KEY_REDIS_KEY = "jwt:secret";

    public RedisKeyService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 매월 1일 자정에 실행 (cron 표현식은 서버 타임존에 따라 조정)
    @Scheduled(cron = "0 0 0 1 * ?")
    public void updateSecretKeyMonthly() {
        String newSecret = CustomUUID.generateUUIDv4();
        redisTemplate.opsForValue().set(SECRET_KEY_REDIS_KEY, newSecret);
        System.out.println("JWT secret key updated: " + newSecret);
    }

    public String getCurrentSecretKey() {
        String key = redisTemplate.opsForValue().get(SECRET_KEY_REDIS_KEY);
        // 키가 없으면, 초기 생성 (예: 어플리케이션 시작 시)
        if (key == null) {
            key = CustomUUID.generateUUIDv4();
            redisTemplate.opsForValue().set(SECRET_KEY_REDIS_KEY, key);
        }
        return key;
    }
}
