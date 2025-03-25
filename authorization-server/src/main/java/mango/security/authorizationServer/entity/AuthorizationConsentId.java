package mango.security.authorizationServer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Table(name = "authorization_consent")
public class AuthorizationConsentId implements Serializable {
    @Column(name="registered_client_id", length = 100, nullable = false)
    private String registeredClientId;

    @Column(name="principal_name", length = 200, nullable = false)
    private String principalName;

    // 기본 생성자 & equals, hashCode 필수!
    public AuthorizationConsentId() {}

    public AuthorizationConsentId(String registeredClientId, String principalName) {
        this.registeredClientId = registeredClientId;
        this.principalName = principalName;
    }

    public String getRegisteredClientId() {
        return registeredClientId;
    }

    public String getPrincipalName() {
        return principalName;
    }

    // equals, hashCode 필수!
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorizationConsentId that)) return false;
        return Objects.equals(registeredClientId, that.registeredClientId)
                && Objects.equals(principalName, that.principalName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registeredClientId, principalName);
    }
}
