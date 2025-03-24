package mango.security.authorizationServer.controller;

import mango.security.authorizationServer.dto.BaseResponseDto;
import mango.security.authorizationServer.dto.ConsentRequestDto;
import mango.security.authorizationServer.service.ConsentService;
import mango.security.authorizationServer.type.ApiResponseType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2/consent")
public class ConsentController {
    private final ConsentService consentService;

    public ConsentController(ConsentService consentService) {
        this.consentService = consentService;
    }

    @GetMapping
    public BaseResponseDto<Object> consentInfo(ConsentRequestDto reqDto) {
        var data = consentService.extractConsentInfo(reqDto);
        return BaseResponseDto.of(ApiResponseType.SUCCESS, data);
    }
}
