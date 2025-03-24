package mango.security.basic_login.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class InMemoryUserDetailsService {

    private final InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final ConcurrentHashMap<String, UserDetails> users = new ConcurrentHashMap<>();

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

    public UserDetailsService getUserDetailsService() {
        return userDetailsManager;
    }
}
