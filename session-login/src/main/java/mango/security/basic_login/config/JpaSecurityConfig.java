package mango.security.basic_login.config;

import jakarta.servlet.http.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import mango.security.basic_login.service.JpaUserDetailsService;

@Configuration
@RequiredArgsConstructor
public class JpaSecurityConfig {
    private final JpaUserDetailsService jpaUserDetailsService;

    @Bean
    public SecurityFilterChain jpSecurityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth-> auth
            .requestMatchers("/jpa/home").authenticated()
            .anyRequest().permitAll()
        )
        .formLogin(form -> form
            .loginPage("/jpa/login")
            .loginProcessingUrl("/jpa/process_login")
            .defaultSuccessUrl("/jpa/home", true)
            .permitAll()
        )
                .logout(logout -> logout
                        .logoutUrl("/jpa/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            Cookie cookie = new Cookie("JSESSIONID", null);
                            cookie.setPath("/");
                            cookie.setHttpOnly(true);
                            cookie.setMaxAge(0);
                            response.addCookie(cookie);
                            response.sendRedirect("/jpa/logout?success");
                        })
                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID") // 세션 삭제
                        .permitAll()
                )

                .userDetailsService(jpaUserDetailsService);

        return http.build();
    }
}
