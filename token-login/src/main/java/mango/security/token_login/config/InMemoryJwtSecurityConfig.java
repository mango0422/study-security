package mango.security.token_login.config;

import mango.security.token_login.security.CustomAccessDeniedHandler;
import mango.security.token_login.security.CustomAuthenticationEntryPoint;
import mango.security.token_login.security.ExceptionHandlingFilter;
import mango.security.token_login.security.JwtInMemoryAuthenticationFilter;
import mango.security.token_login.security.RateLimitingFilter;
import mango.security.token_login.token.InMemoryJwtTokenProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class InMemoryJwtSecurityConfig {

    private final InMemoryJwtTokenProvider jwtTokenProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final UserDetailsService inMemoryUserDetailsService;

    public InMemoryJwtSecurityConfig(InMemoryJwtTokenProvider jwtTokenProvider,
                                     CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                                     CustomAccessDeniedHandler customAccessDeniedHandler,
                                     @Qualifier("inMemoryUserDetailsService") UserDetailsService inMemoryUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.inMemoryUserDetailsService = inMemoryUserDetailsService;
    }

    @Bean
    public SecurityFilterChain inMemorySecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/auth/inmemory/**")
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/auth/inmemory/login", "/api/auth/inmemory/signup").permitAll()
                        .anyRequest().authenticated()
                );

        // 필터 순서: ExceptionHandlingFilter → RateLimitingFilter → JWT 인증 필터
        http.addFilterBefore(new ExceptionHandlingFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new RateLimitingFilter(), JwtInMemoryAuthenticationFilter.class);
        http.addFilterBefore(new JwtInMemoryAuthenticationFilter(jwtTokenProvider, inMemoryUserDetailsService),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
