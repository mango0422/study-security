package mango.security.authorizationServer.repository;

import mango.security.authorizationServer.entity.RegisteredClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegisteredClientJpaRepository extends JpaRepository<RegisteredClientEntity, String> {
    Optional<RegisteredClientEntity> findByClientId(String clientId);
    long countByClientId(String clientId);
}
