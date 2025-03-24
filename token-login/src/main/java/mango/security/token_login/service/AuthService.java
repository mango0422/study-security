package mango.security.token_login.service;

import mango.security.token_login.dto.AuthResponseDto;
import mango.security.token_login.dto.SignUpRequestDto;

public interface AuthService {
    /**
     * 로그인: email과 password를 받아서 accessToken과 refreshToken을 발급합니다.
     * 실패 시 AuthException을 발생시킵니다.
     */
    AuthResponseDto login(String email, String password);

    /**
     * refreshToken을 이용해 새 accessToken (및 옵션에 따라 새 refreshToken)을 발급합니다.
     * 유효하지 않으면 AuthException을 발생시킵니다.
     */
    AuthResponseDto refreshToken(String accessToken, String refreshToken);

    /**
     * 로그아웃: refreshToken 저장소에서 해당 토큰을 제거합니다.
     * 실패 시 AuthException을 발생시킵니다.
     */
    void logout(String accessToken, String refreshToken);

    /**
     * 회원가입: SignUpRequestDto를 받아 신규 사용자를 등록하고, 가입 후 로그인 상태의 토큰을 발급합니다.
     */
    AuthResponseDto signup(SignUpRequestDto signUpRequestDto);
}
