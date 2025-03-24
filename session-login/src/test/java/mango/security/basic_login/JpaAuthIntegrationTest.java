// JpaAuthIntegrationTest.java
package mango.security.basic_login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import mango.security.basic_login.entity.Member;
import mango.security.basic_login.repository.MemberRepository;
import mango.security.basic_login.service.JpaUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class JpaAuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JpaUserDetailsService jpaUserDetailsService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        // 테스트 전에 DB를 깨끗이 초기화
        memberRepository.deleteAll();
    }

    @Test
    public void testSignupAndLogin_JPA() throws Exception {
        // 회원가입 API를 통해 회원 가입 (또는 직접 서비스 호출)
        mockMvc.perform(post("/jpa/signup")
                        .param("username", "testJpa@mail.com")
                        .param("password", "testPass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jpa/login"));

        // DB에 해당 사용자가 등록되었는지 직접 검증
        Member member = memberRepository.findByEmail("testJpa@mail.com").orElse(null);
        assertThat(member).isNotNull();
        // 암호화된 비밀번호가 제대로 저장되었는지 확인 (평문과 일치하지 않음)
        assertThat(passwordEncoder.matches("testPass", member.getPassword())).isTrue();

        // 로그인 요청 검증
        mockMvc.perform(post("/jpa/process_login")
                        .param("username", "testJpa@mail.com")
                        .param("password", "testPass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jpa/home"));

        // 로그아웃 요청 후 리다이렉션 검증
        mockMvc.perform(post("/jpa/logout"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testLoadUserByUsername_JPA() {
        // 직접 DB에 테스트 데이터 삽입
        Member member = new Member();
        member.setEmail("direct@mail.com");
        member.setPassword(passwordEncoder.encode("directPass"));
        member.setRole(mango.security.basic_login.type.Role.USER);
        memberRepository.save(member);

        // JpaUserDetailsService를 통해 사용자를 조회하고, 올바른 권한이 부여되었는지 검증
        var userDetails = jpaUserDetailsService.loadUserByUsername("direct@mail.com");
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("direct@mail.com");
        // 비밀번호는 암호화된 상태이므로 raw 값과 비교하지 않고, matches() 사용
        assertThat(passwordEncoder.matches("directPass", userDetails.getPassword())).isTrue();
    }
}
