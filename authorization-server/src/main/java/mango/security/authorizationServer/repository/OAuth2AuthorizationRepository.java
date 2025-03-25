package mango.security.authorizationServer.repository;

import mango.security.authorizationServer.entity.OAuth2AuthorizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OAuth2AuthorizationRepository extends JpaRepository<OAuth2AuthorizationEntity, String> {
    Optional<OAuth2AuthorizationEntity> findByRefreshTokenValue(String refreshTokenValue);
}
