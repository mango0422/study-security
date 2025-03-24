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
        Assert.notBlank(dto.client_id(), ApiResponseType.INVALID_REQUEST);
        Assert.notBlank(dto.scope(), ApiResponseType.INVALID_REQUEST);

        var client = clientRepository.findByClientId(dto.client_id())
                .orElseThrow(() -> new AuthException(ApiResponseType.CLIENT_NOT_FOUND));

        return Map.of(
                "clientName", client.getClientName(),
                "requestedScope", dto.scope(),
                "redirectedUri", dto.redirect_uri()
        );
    }
}
