package mango.security.authorizationServer.config;

import mango.security.authorizationServer.dto.StoredTokenDto;
import mango.security.authorizationServer.service.TokenPersistenceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.time.Instant;
import java.util.UUID;

@Configuration
public class OAuth2TokenCustomizerConfig {
    @Value("${token.refreshToken.ttl}")
    private long rereshTokenTtl;

    private final TokenPersistenceService tokenPersistenceService;

    public OAuth2TokenCustomizerConfig(TokenPersistenceService tokenPersistenceService) {
        this.tokenPersistenceService = tokenPersistenceService;
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            // OAuth2AuthorizationServerConfigurer 내에서 access token 발급 시 이 커스터마이저가 호출됩니다.
            if ("access_token".equals(context.getTokenType().getValue())) {
                JwtClaimsSet.Builder claimsBuilder = context.getClaims();
                JwtClaimsSet claims = claimsBuilder.build();
                Instant issuedAt = claims.getIssuedAt();
                Instant expiresAt = claims.getExpiresAt();

//                String accessTokenValue = context.getTokenValue();
                String accessTokenValue;
                if (claims.getClaim("jti") != null) {
                    accessTokenValue = claims.getClaim("jti").toString();
                } else {
                    accessTokenValue = UUID.randomUUID().toString();
                    claimsBuilder.claim("jti", accessTokenValue);
                }
                String principalName = context.getPrincipal().getName();
                String clientId = context.getRegisteredClient().getClientId();

                // Access token 정보 DTO 생성
                StoredTokenDto accessTokenDto = new StoredTokenDto(
                        accessTokenValue,
                        principalName,
                        clientId,
                        "access",
                        issuedAt,
                        expiresAt
                );

                String refreshTokenValue = UUID.randomUUID().toString();
                // refresh token 발급 시점과 만료 시간은 정책에 따라 정합니다.
                Instant refreshIssuedAt = Instant.now();
                // refreshTokenTtl는 일(day) 단위로 설정됨 (예: 7일)
                long ttl = rereshTokenTtl * 60 * 60 * 24;
                Instant refreshExpiresAt = refreshIssuedAt.plusSeconds(ttl); // 예: 1일
                StoredTokenDto refreshTokenDto = new StoredTokenDto(
                        refreshTokenValue,
                        principalName,
                        clientId,
                        "refresh",
                        refreshIssuedAt,
                        refreshExpiresAt
                );

                // 토큰 쌍 저장
                tokenPersistenceService.persistTokenPair(accessTokenDto, refreshTokenDto);
            }
        };
    }
}
