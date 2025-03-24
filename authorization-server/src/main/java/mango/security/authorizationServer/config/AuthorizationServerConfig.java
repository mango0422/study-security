package mango.security.authorizationServer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.time.Instant;
import java.util.UUID;

@Configuration
public class AuthorizationServerConfig {
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        JdbcRegisteredClientRepository repository = new JdbcRegisteredClientRepository(jdbcTemplate);
        String clientId = "mango-client";
        if(repository.findByClientId(clientId) == null) {
            RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId(clientId)
                    .clientSecret(passwordEncoder.encode("mango-secret"))
                    .clientIdIssuedAt(Instant.now())
                    .clientName("Mango Local Client")
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .redirectUri("http://localhost:3000/login/oauth2/code/mango-client")
                    .scope("read")
                    .scope("write")
                    .build();
            repository.save(registeredClient);
        }
        return repository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // 단편적으로 봤을때는 BCrypotPasswordEncoder를 사용하는 것과 동일
        // 하지만 향후 확장성을 고려하거나 Spring 권장 방식을 따르기에
        // PasswordEncoderFactories.createDelegatingPasswordEncoder()를 사용하는 것이 좋다.
    }
}
