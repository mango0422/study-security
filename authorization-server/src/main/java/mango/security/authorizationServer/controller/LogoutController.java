package mango.security.authorizationServer.controller;

import mango.security.authorizationServer.dto.BaseResponseDto;
import mango.security.authorizationServer.service.TokenRedisService;
import mango.security.authorizationServer.type.ApiResponseType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logout")
public class LogoutController {

    private final TokenRedisService tokenRedisService;

    public LogoutController(TokenRedisService tokenRedisService) {
        this.tokenRedisService = tokenRedisService;
    }

    /**
     * 로그아웃 시 access token과 refresh token을 철회(삭제) 처리하는 API
     * 클라이언트는 accessToken, refreshToken 값을 전달해야 합니다.
     */
    @DeleteMapping
    public BaseResponseDto<Object> logout(String accessToken, String refreshToken) {
        tokenRedisService.deleteTokenPair(accessToken, refreshToken);
        return BaseResponseDto.of(ApiResponseType.SUCCESS, "로그아웃 및 토큰 철회가 완료되었습니다.");
    }
}
