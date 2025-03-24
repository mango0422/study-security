package mango.security.token_login.service;

import mango.security.token_login.dto.AuthResponseDto;
import mango.security.token_login.exception.AuthException;
import mango.security.token_login.token.JwtTokenProvider;
import mango.security.token_login.token.RefreshTokenProvider;
import mango.security.token_login.token.RefreshTokenStrategy;
import mango.security.token_login.type.ApiResponseType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class AbstractAuthService implements AuthService {

    protected final JwtTokenProvider jwtTokenProvider;
    protected final RefreshTokenProvider refreshTokenProvider;
    protected final PasswordEncoder passwordEncoder;
    protected final RefreshTokenStrategy refreshTokenStrategy;

    // 하위 구현체가 공급해야 하는 UserDetailsService
    protected abstract UserDetailsService getUserDetailsService();

    public AbstractAuthService(JwtTokenProvider jwtTokenProvider,
                               RefreshTokenProvider refreshTokenProvider,
                               PasswordEncoder passwordEncoder,
                               RefreshTokenStrategy refreshTokenStrategy) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenStrategy = refreshTokenStrategy;
    }

    @Override
    public AuthResponseDto login(String email, String password) {
        UserDetails user = getUserDetailsService().loadUserByUsername(email);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException(ApiResponseType.LOGIN_FAILED, "비밀번호가 일치하지 않습니다.");
        }
        String accessToken = jwtTokenProvider.generateToken(email);
        String refreshToken = refreshTokenProvider.createRefreshToken(accessToken, refreshTokenStrategy);
        return new AuthResponseDto(accessToken, refreshToken);
    }

    @Override
    public AuthResponseDto refreshToken(String accessToken, String refreshToken) {
        boolean valid = refreshTokenProvider.validate(accessToken, refreshToken, refreshTokenStrategy);
        if (!valid) {
            throw new AuthException(ApiResponseType.REFRESH_TOKEN_INVALID, "유효하지 않은 리프레시 토큰입니다.");
        }
        String username;
        try {
            username = jwtTokenProvider.getUsername(accessToken);
        } catch (Exception e) {
            throw new AuthException(ApiResponseType.OUTDATED_TOKEN, "액세스 토큰이 만료되었습니다.");
        }
        String newAccessToken = jwtTokenProvider.generateToken(username);
        // 보안을 위해 새 리프레시 토큰도 발급
        String newRefreshToken = refreshTokenProvider.createRefreshToken(newAccessToken, refreshTokenStrategy);
        return new AuthResponseDto(newAccessToken, newRefreshToken);
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        refreshTokenProvider.remove(accessToken, refreshTokenStrategy);
    }
}
