package mango.security.authorizationServer.config;

import mango.security.authorizationServer.entity.UserEntity;
import mango.security.authorizationServer.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 예: admin 계정이 없으면 생성
            if (userRepository.findByUsername("admin").isEmpty()) {
                UserEntity user = UserEntity.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("adminpassword"))
                        .roles("ROLE_ADMIN,ROLE_USER")
                        .build();
                userRepository.save(user);
            }
        };
    }
}
