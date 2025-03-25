package mango.security.authorizationServer.config;

import mango.security.authorizationServer.filter.PreflightClientCheckFilter;
import mango.security.authorizationServer.repository.RegisteredClientJpaRepository;
import mango.security.authorizationServer.security.CustomAccessDeniedHandler;
import mango.security.authorizationServer.security.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler, RegisteredClientJpaRepository registeredClientJpaRepository) throws Exception {
        http
            .addFilterBefore(new PreflightClientCheckFilter(registeredClientJpaRepository), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .exceptionHandling(exceptions -> exceptions
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    .accessDeniedHandler(customAccessDeniedHandler)
            )
            .formLogin(Customizer.withDefaults()) // form login for test
            // 로그아웃은 별도의 LogoutController를 통해 처리하므로 기본 로그아웃 URL은 비활성화하거나 설정만 해둡니다.
            .logout(logout -> logout
                    .logoutUrl("/api/logout")
                    .logoutSuccessHandler((request, response, authentication) -> {
                        // 로그아웃 성공 후 별도 처리는 LogoutController에서 담당
                    })
            );

        return http.build();
    }
}