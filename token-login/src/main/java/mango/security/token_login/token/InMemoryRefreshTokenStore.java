package mango.security.token_login.token;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mango.security.token_login.token.RefreshTokenProvider.RefreshTokenStore;

@Component
@Qualifier("inMemory")
public class InMemoryRefreshTokenStore implements RefreshTokenStore {
    private static class TokenInfo {
        String refreshToken;
        long expiresAt;

        TokenInfo(String refreshToken, long expiresAt) {
            this.refreshToken = refreshToken;
            this.expiresAt = expiresAt;
        }
    }

    private final Map<String, TokenInfo> store = new ConcurrentHashMap<>();

    @Override public RefreshTokenStrategy strategy() {
        return RefreshTokenStrategy.IN_MEMORY;
    }

    @Override
    public void save(String accessToken, String refreshToken, long ttl) {
        long expiresAt = System.currentTimeMillis() + ttl;
        store.put(accessToken, new TokenInfo(refreshToken, expiresAt));
    }

    @Override
    public boolean validate(String accessToken, String refreshToken) {
        TokenInfo info = store.get(accessToken);
        if(info == null) {
            return false;
        }
        if(System.currentTimeMillis() >= info.expiresAt){
            store.remove(accessToken);
            return false;
        }
        return info.refreshToken.equals(refreshToken);
    }

    @Override
    public void remove(String accessToken) {
        store.remove(accessToken);
    }
}
