package mango.security.token_login.config;

import mango.security.token_login.security.CustomAccessDeniedHandler;
import mango.security.token_login.security.CustomAuthenticationEntryPoint;
import mango.security.token_login.security.ExceptionHandlingFilter;
import mango.security.token_login.security.JwtJpaAuthenticationFilter;
import mango.security.token_login.security.RateLimitingFilter;
import mango.security.token_login.token.JwtTokenProvider;
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
public class JpaJwtSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final UserDetailsService jpaUserDetailsService;

    public JpaJwtSecurityConfig(JwtTokenProvider jwtTokenProvider,
                                CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                                CustomAccessDeniedHandler customAccessDeniedHandler,
                                @Qualifier("jpaUserDetailsService") UserDetailsService jpaUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    @Bean
    public SecurityFilterChain jpSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/auth/jpa/**")
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/jpa/login", "/api/auth/jpa/signup").permitAll()
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(new ExceptionHandlingFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new RateLimitingFilter(), JwtJpaAuthenticationFilter.class);
        http.addFilterBefore(new JwtJpaAuthenticationFilter(jwtTokenProvider, jpaUserDetailsService),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
