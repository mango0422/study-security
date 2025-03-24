package mango.security.basic_login.service;

import java.util.Collections;
import java.util.Optional;

import mango.security.basic_login.type.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mango.security.basic_login.entity.Member;
import mango.security.basic_login.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        } else {
            return new User(member.get().getEmail(), member.get().getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + member.get().getRole().name())));
        }
    }

    public void addUser(String email, String password) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("User already exists");
        }
        Member member = new Member();
        member.setEmail(email);
        member.setPassword(passwordEncoder.encode(password));
        member.setRole(Role.USER);
        memberRepository.save(member);
    }
}
