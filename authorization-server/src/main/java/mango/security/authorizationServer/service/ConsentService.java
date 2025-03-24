package mango.security.authorizationServer.service;

import mango.security.authorizationServer.dto.ConsentRequestDto;
import mango.security.authorizationServer.exception.AuthException;
import mango.security.authorizationServer.repository.RegisteredClientJpaRepository;
import mango.security.authorizationServer.type.ApiResponseType;
import mango.security.authorizationServer.util.Assert;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConsentService {

    private final RegisteredClientJpaRepository clientRepository;

    public ConsentService(RegisteredClientJpaRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Map<String, String> extractConsentInfo(ConsentRequestDto dto) {
        Assert.notBlank(dto.client_id(), ApiResponseType.INVALID_REQUEST, "client_id는 필수입니다.");
        Assert.notBlank(dto.scope(), ApiResponseType.INVALID_REQUEST, "scope는 필수입니다.");

        var client = clientRepository.findByClientId(dto.client_id())
                .orElseThrow(() -> new AuthException(ApiResponseType.CLIENT_NOT_FOUND, "등록되지 않은 클라이언트입니다."));

        return Map.of(
                "clientName", client.getClientName(),
                "requestedScope", dto.scope(),
                "redirectedUri", dto.redirect_uri()
        );
    }
}
