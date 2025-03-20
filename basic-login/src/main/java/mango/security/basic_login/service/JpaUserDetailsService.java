package mango.security.basic_login.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mango.security.basic_login.entity.Member;
import mango.security.basic_login.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService{

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Member member = memberRepository.findByEmail(email);
        if (member == null){
            throw new UsernameNotFoundException("User not found");
        } else {
            return new User(member.getEmail(), member.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + member.getRole().name())));
        }
    }
}
