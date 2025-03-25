package mango.security.authorizationServer.service;

import mango.security.authorizationServer.dto.StoredTokenDto;
import mango.security.authorizationServer.entity.OAuth2AuthorizationEntity;
import mango.security.authorizationServer.exception.AuthException;
import mango.security.authorizationServer.repository.OAuth2AuthorizationRepository;
import mango.security.authorizationServer.type.ApiResponseType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final TokenRedisService tokenRedisService;
    private final OAuth2AuthorizationRepository authorizationRepository;

    public RefreshTokenServiceImpl(TokenRedisService tokenRedisService,
                                OAuth2AuthorizationRepository authorizationRepository) {
        this.tokenRedisService = tokenRedisService;
        this.authorizationRepository = authorizationRepository;
    }

    @Override
    public StoredTokenDto getRefreshToken(String refreshTokenValue) {
        try {
            // 먼저 Redis에서 조회
            return tokenRedisService.getToken(refreshTokenValue, "refresh_token");
        } catch (AuthException ex) {
            // Redis에 없으면 DB 조회
            Optional<OAuth2AuthorizationEntity> tmp = authorizationRepository.findByRefreshTokenValue(refreshTokenValue);
            if (tmp.isEmpty()) {
                throw new AuthException(ApiResponseType.INVALID_TOKEN, "DB에 refresh token이 존재하지 않습니다.");
            }
            OAuth2AuthorizationEntity entity = tmp.get();
            StoredTokenDto storedTokenDto = convertEntityToStoredToken(entity);
            // DB에서 조회한 토큰을 Redis에 재저장 (선택 사항)
            tokenRedisService.saveToken(storedTokenDto);
            return storedTokenDto;
        }
    }

    private StoredTokenDto convertEntityToStoredToken(OAuth2AuthorizationEntity entity) {
        return new StoredTokenDto(
                entity.getRefreshTokenValue(),
                entity.getPrincipalName(),
                entity.getClientId(),
                "refresh",
                entity.getRefreshTokenIssuedAt(),
                entity.getRefreshTokenExpiresAt()
        );
    }
}
