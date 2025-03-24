package mango.security.token_login.token;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import mango.security.token_login.util.SecretKeyUtil;

@Getter
@Component("inMemorySecretKeyHolder")
public class InMemorySecretKeyHolder {
    private static final Logger logger = LoggerFactory.getLogger(InMemorySecretKeyHolder.class);
    private String secretKey;

    public InMemorySecretKeyHolder() {
        // 애플리케이션 시작 시 초기 secret key 생성
        this.secretKey = SecretKeyUtil.generateSecretKey();
    }

    // 매월 1일 자정에 secret key 갱신 (서버 시간 기준)
    @Scheduled(cron = "0 0 0 1 * ?")
    public void updateSecretKey() {
        secretKey = SecretKeyUtil.generateSecretKey();
        logger.info("인메모리 JWT secret key 갱신됨: {}", secretKey);
    }
}
