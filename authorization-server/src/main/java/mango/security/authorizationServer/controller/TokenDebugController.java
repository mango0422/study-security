package mango.security.authorizationServer.controller;

import mango.security.authorizationServer.dto.BaseResponseDto;
import mango.security.authorizationServer.dto.StoredTokenDto;
import mango.security.authorizationServer.dto.TokenPairDto;
import mango.security.authorizationServer.service.TokenRedisService;
import mango.security.authorizationServer.type.ApiResponseType;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/debug/token")
public class TokenDebugController {

    private final TokenRedisService tokenRedisService;

    public TokenDebugController(TokenRedisService tokenRedisService) {
        this.tokenRedisService = tokenRedisService;
    }

    @PostMapping("/save")
    public BaseResponseDto<Object> saveToken(@RequestParam("type") String type) {
        Instant now = Instant.now();
        StoredTokenDto token = new StoredTokenDto(
                UUID.randomUUID().toString(),
                "user@example.com",
                "mango-client",
                type,
                now,
                now.plusSeconds(type.equals("access") ? 600 : 86400)
        );
        tokenRedisService.saveToken(token);
        return BaseResponseDto.of(ApiResponseType.SUCCESS, token);
    }

    @PostMapping("/save-pair")
    public BaseResponseDto<Object> saveTokenPair() {
        Instant now = Instant.now();
        String access = UUID.randomUUID().toString();
        String refresh = UUID.randomUUID().toString();

        StoredTokenDto accessToken = new StoredTokenDto(
                access, "user@example.com", "mango-client", "access", now, now.plusSeconds(600));
        StoredTokenDto refreshToken = new StoredTokenDto(
                refresh, "user@example.com", "mango-client", "refresh", now, now.plusSeconds(86400));

        tokenRedisService.saveTokenPair(new TokenPairDto(accessToken, refreshToken));

        return BaseResponseDto.of(ApiResponseType.SUCCESS, Map.of(
                "accessToken", access,
                "refreshToken", refresh
        ));
    }

    @GetMapping("/get")
    public BaseResponseDto<Object> getToken(@RequestParam("type") String type, @RequestParam("value") String value) {
        var token = tokenRedisService.getToken(value, type);
        return BaseResponseDto.of(ApiResponseType.SUCCESS, token);
    }

    @DeleteMapping("/delete")
    public BaseResponseDto<Object> deleteToken(@RequestParam("type") String type, @RequestParam("value") String value) {
        tokenRedisService.deleteToken(value, type);
        return BaseResponseDto.of(ApiResponseType.SUCCESS, "삭제됨");
    }

    @DeleteMapping("/delete-pair")
    public BaseResponseDto<Object> deletePair(@RequestParam("access") String access, @RequestParam("refresh") String refresh) {
        tokenRedisService.deleteTokenPair(access, refresh);
        return BaseResponseDto.of(ApiResponseType.SUCCESS, "쌍 삭제 완료");
    }
}
