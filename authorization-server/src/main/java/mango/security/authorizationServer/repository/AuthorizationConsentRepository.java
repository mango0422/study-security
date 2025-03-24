package mango.security.authorizationServer.repository;

import mango.security.authorizationServer.entity.AuthorizationConsentId;
import mango.security.authorizationServer.entity.OAuth2AuthorizationConsentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorizationConsentRepository
        extends JpaRepository<OAuth2AuthorizationConsentEntity, AuthorizationConsentId> {

    Optional<OAuth2AuthorizationConsentEntity> findById(AuthorizationConsentId id);
}
