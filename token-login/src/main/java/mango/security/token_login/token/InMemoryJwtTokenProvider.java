package mango.security.token_login.token;

import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Component("inMemoryJwtTokenProvider")
public class InMemoryJwtTokenProvider extends AbstractJwtTokenProvider {

    private final InMemorySecretKeyHolder secretKeyHolder;
    private Key key;

    public InMemoryJwtTokenProvider(InMemorySecretKeyHolder secretKeyHolder) {
        this.secretKeyHolder = secretKeyHolder;
    }

    @PostConstruct
    public void init() {
        updateKey();
    }

    // in-memory 방식은 매 호출 시 최신 secret key를 반영하도록 함
    private void updateKey() {
        String secret = secretKeyHolder.getSecretKey();
        key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    @Override
    protected Key getKey() {
        updateKey();
        return key;
    }
}
