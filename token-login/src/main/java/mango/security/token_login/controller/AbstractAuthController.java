package mango.security.token_login.controller;

import mango.security.token_login.dto.AuthResponseDto;
import mango.security.token_login.dto.BaseResponseDto;
import mango.security.token_login.type.ApiResponseType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;

public abstract class AbstractAuthController {

    // 공통: 쿠키 생성 메서드 (Spring ResponseCookie 사용)
    protected void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true) // 프로덕션에서는 HTTPS 환경에서 true
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("None")
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
    }

    // 공통: 쿠키 삭제 메서드
    protected void deleteRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
    }

    // 공통: 응답 포맷 생성 메서드
    protected <T> ResponseEntity<BaseResponseDto<T>> buildResponse(T data, ApiResponseType responseType) {
        return ResponseEntity.ok(new BaseResponseDto<>(responseType, data));
    }
}
