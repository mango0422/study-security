package mango.security.token_login.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RefreshTokenProvider {

    private final Map<RefreshTokenStrategy, RefreshTokenStore> strategyMap;

    @Value("${jwt.refreshToken.ttl}")
    private long refreshExpireTime;

    public RefreshTokenProvider(List<RefreshTokenStore> storeList) {
        this.strategyMap = storeList.stream()
                .collect(Collectors.toMap(RefreshTokenStore::strategy, store -> store));
    }

    public String createRefreshToken(String accessToken, RefreshTokenStrategy strategy) {
        String refreshToken = CustomUUID.generateUUIDv4();
        getStore(strategy).save(accessToken, refreshToken, refreshExpireTime);
        return refreshToken;
    }

    public boolean validate(String accessToken, String refreshToken, RefreshTokenStrategy strategy) {
        return getStore(strategy).validate(accessToken, refreshToken);
    }

    public void remove(String accessToken, RefreshTokenStrategy strategy) {
        getStore(strategy).remove(accessToken);
    }

    private RefreshTokenStore getStore(RefreshTokenStrategy strategy) {
        RefreshTokenStore store = strategyMap.get(strategy);
        if (store == null) {
            throw new IllegalArgumentException("No RefreshTokenStore found for strategy: " + strategy);
        }
        return store;
    }

    public interface RefreshTokenStore {
        RefreshTokenStrategy strategy();
        void save(String accessToken, String refreshToken, long ttl); // TTL 단위로
        boolean validate(String accessToken, String refreshToken);     // 값 일치 확인
        void remove(String accessToken);
    }
}
