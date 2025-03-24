package mango.security.authorizationServer.service;

import mango.security.authorizationServer.dto.ConsentConfirmRequestDto;
import mango.security.authorizationServer.repository.AuthorizationConsentRepository;
import mango.security.authorizationServer.repository.RegisteredClientJpaRepository;
import mango.security.authorizationServer.type.ApiResponseType;
import mango.security.authorizationServer.util.Assert;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationConsentService {
    private final AuthorizationConsentRepository authorizationConsentRepository;
    private final RegisteredClientJpaRepository registeredClientJpaRepository;

    public AuthorizationConsentService(AuthorizationConsentRepository authorizationConsentRepository,
            RegisteredClientJpaRepository registeredClientJpaRepository) {
        this.authorizationConsentRepository = authorizationConsentRepository;
        this.registeredClientJpaRepository = registeredClientJpaRepository;
    }

    public void confirmConsent(ConsentConfirmRequestDto dto){
        Assert.notBlank(dto.client_id(), ApiResponseType.INVALID_REQUEST);
    }
}
