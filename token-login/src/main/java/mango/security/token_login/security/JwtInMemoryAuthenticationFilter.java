package mango.security.token_login.security;

import mango.security.token_login.token.InMemoryJwtTokenProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;

public class JwtInMemoryAuthenticationFilter extends AbstractJwtAuthenticationFilter {
    public JwtInMemoryAuthenticationFilter(InMemoryJwtTokenProvider jwtTokenProvider,
                                           @Qualifier("inMemoryUserDetailsService") UserDetailsService userDetailsService) {
        super(jwtTokenProvider, userDetailsService);
    }
}
