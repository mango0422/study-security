package mango.security.basic_login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class InMemorySecurityConfig {

    @Bean
    public UserDetailsService InMemoryUserDetailsService(){
        UserDetails user = User.builder()
                .username("user")
                // .password("{noop}password")
                .password(passwordEncoder().encode("user"))
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                // .password("{noop}password")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();
        UserDetails manager = User.builder()
                .username("manager")
                // .password("{noop}password")
                .password(passwordEncoder().encode("manager"))
                .roles("MANAGER")
                .build();
        return new InMemoryUserDetailsManager(user, admin, manager);
    } // 유저 등록. {noop}은 암호화를 하지 않는다는 의미

    @Bean
    public SecurityFilterChain inMemorySecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                    .requestMatchers("/inmemory/**").authenticated()
                    .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/inmemory/login")
                .loginProcessingUrl("/inmemory/process_login")
                .defaultSuccessUrl("/inmemory/home")
                .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/inmemory/logout")
                    .logoutSuccessUrl("/inmemory/login?logout")
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
