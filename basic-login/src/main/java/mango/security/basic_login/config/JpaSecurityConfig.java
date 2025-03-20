package mango.security.basic_login.config;

import org.checkerframework.checker.units.qual.A;
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
            .requestMatchers("/jpa/**").authenticated()
            .anyRequest().permitAll()
        )
        .formLogin(form -> form
            .loginPage("/jpa/login")
            .loginProcessingUrl("/jpa/process_login")
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/jpa/logout")
            .logoutSuccessUrl("/jpa/login?logout")
        )
        .userDetailsService(jpaUserDetailsService);

        return http.build();
    }
}
