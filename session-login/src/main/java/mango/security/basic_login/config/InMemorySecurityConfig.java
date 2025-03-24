package mango.security.basic_login.config;

import jakarta.servlet.http.Cookie;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@NoArgsConstructor
public class InMemorySecurityConfig {

    @Bean
    public SecurityFilterChain inMemorySecurityFilterChain(HttpSecurity http, InMemoryUserDetailsManager userDetailsManager) throws Exception {
        http
                .securityMatcher("/inmemory/**")
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/inmemory/login", "/inmemory/signup").permitAll()
                        .requestMatchers("/inmemory/home").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/inmemory/login")
                        .loginProcessingUrl("/inmemory/process_login")
                        .defaultSuccessUrl("/inmemory/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/inmemory/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            Cookie cookie = new Cookie("JSESSIONID", null);
                            cookie.setPath("/");
                            cookie.setHttpOnly(true);
                            cookie.setMaxAge(0);
                            response.addCookie(cookie);
                            response.sendRedirect("/inmemory/logout?success");
                        })
                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID") // 세션 삭제
                )
                .userDetailsService(userDetailsManager); // 🔥 userDetailsManager를 직접 사용

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔥 초기 사용자 등록을 여기서 수행
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(List.of(user, admin));
    }
}
