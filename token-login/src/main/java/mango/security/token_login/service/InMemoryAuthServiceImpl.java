package mango.security.token_login.service;

import mango.security.token_login.dto.AuthResponseDto;
import mango.security.token_login.token.JwtTokenProvider;
import mango.security.token_login.token.RefreshTokenProvider;
import mango.security.token_login.token.RefreshTokenStrategy;
import mango.security.token_login.dto.SignUpRequestDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service("inMemoryAuthService")
public class InMemoryAuthServiceImpl extends AbstractAuthService {

    private final UserDetailsService inMemoryUserDetailsService;

    public InMemoryAuthServiceImpl(
            @Qualifier("inMemoryUserDetailsService") UserDetailsService inMemoryUserDetailsService,
            JwtTokenProvider jwtTokenProvider,
            RefreshTokenProvider refreshTokenProvider,
            PasswordEncoder passwordEncoder
    ) {
        super(jwtTokenProvider, refreshTokenProvider, passwordEncoder, RefreshTokenStrategy.IN_MEMORY);
        this.inMemoryUserDetailsService = inMemoryUserDetailsService;
    }

    @Override
    protected UserDetailsService getUserDetailsService() {
        return inMemoryUserDetailsService;
    }

    @Override
    public AuthResponseDto signup(SignUpRequestDto signUpRequestDto) {
        // 신규 사용자 추가 (기본 USER 역할)
        ((InMemoryUserDetailsService) inMemoryUserDetailsService)
                .addUser(signUpRequestDto.email(), signUpRequestDto.password(), "USER");
        // 회원가입 후 자동 로그인하여 토큰 발급
        return login(signUpRequestDto.email(), signUpRequestDto.password());
    }
}
