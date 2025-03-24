package mango.security.authorizationServer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "oauth2_authorization_consent")
public class OAuth2AuthorizationConsentEntity {
    @EmbeddedId
    private AuthorizationConsentId id;

    @Column(name = "authorities", nullable = false, length = 1000)
    private String authorities;

    public OAuth2AuthorizationConsentEntity() {}

    public OAuth2AuthorizationConsentEntity(AuthorizationConsentId id, String authorities) {
        this.id = id;
        this.authorities = authorities;
    }

    public AuthorizationConsentId getId() {
        return id;
    }

    public void setId(AuthorizationConsentId id) {
        this.id = id;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }
}
