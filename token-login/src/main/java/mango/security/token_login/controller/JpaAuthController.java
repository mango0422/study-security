package mango.security.token_login.controller;

import mango.security.token_login.dto.AuthResponseDto;
import mango.security.token_login.dto.BaseResponseDto;
import mango.security.token_login.dto.LoginRequestDto;
import mango.security.token_login.dto.SignUpRequestDto;
import mango.security.token_login.service.AuthService;
import mango.security.token_login.type.ApiResponseType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth/jpa")
public class JpaAuthController extends AbstractAuthController {

    private final AuthService authService;

    public JpaAuthController(@Qualifier("jpaAuthService") AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponseDto<AuthResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto,
                                                                  HttpServletResponse response) {
        AuthResponseDto authResponse = authService.login(loginRequestDto.email(), loginRequestDto.password());
        addRefreshTokenCookie(response, authResponse.refreshToken());
        AuthResponseDto result = new AuthResponseDto(authResponse.accessToken(), null);
        return buildResponse(result, ApiResponseType.SUCCESS);
    }

    @PostMapping("/signup")
    public ResponseEntity<BaseResponseDto<AuthResponseDto>> signup(@RequestBody SignUpRequestDto signUpRequestDto,
                                                                   HttpServletResponse response) {
        AuthResponseDto authResponse = authService.signup(signUpRequestDto);
        addRefreshTokenCookie(response, authResponse.refreshToken());
        AuthResponseDto result = new AuthResponseDto(authResponse.accessToken(), null);
        return buildResponse(result, ApiResponseType.SUCCESS);
    }

    @PostMapping("/refresh")
    public ResponseEntity<BaseResponseDto<AuthResponseDto>> refresh(@RequestHeader("Authorization") String accessToken,
                                                                    @CookieValue("refreshToken") String refreshToken) {
        String rawAccessToken = accessToken.replace("Bearer ", "");
        AuthResponseDto authResponse = authService.refreshToken(rawAccessToken, refreshToken);
        return buildResponse(authResponse, ApiResponseType.SUCCESS);
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponseDto<Void>> logout(@RequestHeader("Authorization") String accessToken,
                                                        @CookieValue("refreshToken") String refreshToken,
                                                        HttpServletResponse response) {
        String rawAccessToken = accessToken.replace("Bearer ", "");
        authService.logout(rawAccessToken, refreshToken);
        deleteRefreshTokenCookie(response);
        return buildResponse(null, ApiResponseType.SUCCESS);
    }
}
