package mango.security.authorizationServer.service;

import mango.security.authorizationServer.dto.ConsentConfirmRequestDto;
import mango.security.authorizationServer.entity.AuthorizationConsentId;
import mango.security.authorizationServer.entity.OAuth2AuthorizationConsentEntity;
import mango.security.authorizationServer.exception.AuthException;
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
        // 필수값 검증
        Assert.notBlank(dto.client_id(), ApiResponseType.CLIENT_ID_REQUIRED);
        Assert.notBlank(dto.principal_name(), ApiResponseType.PRINCIPAL_NAME_REQUIRED);
        Assert.notBlank(dto.scope(), ApiResponseType.SCOPE_REQUIRED);

        // 클라이언트 유효성 확인
        boolean exists =registeredClientJpaRepository.existsById(dto.client_id());
        if(!exists){
            throw new AuthException(ApiResponseType.CLIENT_NOT_FOUND);
        }

        // 이미 동의한 경우 예외
        AuthorizationConsentId id = new AuthorizationConsentId(dto.client_id(), dto.principal_name());
        if (authorizationConsentRepository.findById(id).isPresent()){
            throw new AuthException(ApiResponseType.CLIENT_ALREADY_CONSENTED);
        }

        // 동의 정보 저장
        OAuth2AuthorizationConsentEntity entity = new OAuth2AuthorizationConsentEntity(
                id,
                dto.scope() // read, write
        );

        authorizationConsentRepository.save(entity);
    }
}
