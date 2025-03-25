package mango.security.authorizationServer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "oauth2_authorization_consent")
public class OAuth2AuthorizationConsentEntity {

    @EmbeddedId
    private AuthorizationConsentId id;

    @Column(name = "authorities", nullable = false, length = 1000)
    private String authorities;

    // 추가 필드의 getter/setter
    // 추가 필드들
    @Column(name = "refresh_token_value")
    private String refreshTokenValue;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "refresh_token_issued_at")
    private Instant refreshTokenIssuedAt;

    @Column(name = "refresh_token_expires_at")
    private Instant refreshTokenExpiresAt;

    public OAuth2AuthorizationConsentEntity() {}

    public OAuth2AuthorizationConsentEntity(AuthorizationConsentId id, String authorities) {
        this.id = id;
        this.authorities = authorities;
    }

}
