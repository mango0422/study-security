package mango.security.token_login.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service("inMemoryUserDetailsService")
@RequiredArgsConstructor
public class InMemoryUserDetailsService implements UserDetailsService {

    private InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final ConcurrentHashMap<String, UserDetails> users = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
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

        // 기본 사용자 목록으로 InMemoryUserDetailsManager 초기화
        this.userDetailsManager = new InMemoryUserDetailsManager(List.of(user, admin));
        users.put("user", user);
        users.put("admin", admin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userDetailsManager.loadUserByUsername(username);
    }

    public void addUser(String username, String password, String role) {
        if (users.containsKey(username)) {
            throw new IllegalStateException("User already exists");
        }
        UserDetails user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(role)
                .build();
        userDetailsManager.createUser(user);
        users.put(username, user);
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }
}
