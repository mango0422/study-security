package mango.security.token_login.security;

import mango.security.token_login.token.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;

public class JwtJpaAuthenticationFilter extends AbstractJwtAuthenticationFilter {
    public JwtJpaAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                      @Qualifier("jpaUserDetailsService") UserDetailsService userDetailsService) {
        super(jwtTokenProvider, userDetailsService);
    }
}
