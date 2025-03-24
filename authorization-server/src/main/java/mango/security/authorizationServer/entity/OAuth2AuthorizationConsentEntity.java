package mango.security.authorizationServer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class OAuth2AuthorizationConsentEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 원래는 (registered_client_id, principal_name)를 복합키로 설정할 수도 있으나,
    // 간편화를 위해 surrogate key(id)를 사용.
    // Spring Authorization Server는 복합 키를 기본으로 사용하지만,
    // 우리는 직접 관리할 것이므로 무방.
    @Column(name = "registered_client_id", nullable = false, length = 100)
    private String registeredClientId;

    @Column(name = "principal_name", nullable = false, length = 200)
    private String principal_name;

    @Column(name = "authorities", nullable = false, length = 1000)
    private String Authorities;
}
