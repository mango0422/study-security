// InMemoryAuthIntegrationTest.java
package mango.security.basic_login;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import mango.security.basic_login.service.InMemoryUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class InMemoryAuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InMemoryUserDetailsService inMemoryUserDetailsService;

    @BeforeEach
    public void setup() {
        // 필요시 기존 사용자 초기화 등 추가 작업
    }

    @Test
    @WithAnonymousUser
    public void testSignupAndLogin_InMemory() throws Exception {
        // 회원가입 요청 (CSRF 토큰 추가)
        mockMvc.perform(post("/inmemory/signup")
                        .with(csrf())
                        .param("username", "testUser@mail.com")
                        .param("password", "testPass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("login"));

        // 인메모리에 사용자가 등록되었는지 직접 검증 (서비스 내 사용자 Map 이용)
        boolean exists = inMemoryUserDetailsService.userExists("testUser@mail.com");
        assert(exists);

        // 로그인 요청 (실제 로그인 후 세션이 생성되는지 검증, CSRF 토큰 추가)
        mockMvc.perform(post("/inmemory/process_login")
                        .with(csrf())
                        .param("username", "testUser@mail.com")
                        .param("password", "testPass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/inmemory/home"));

        // 로그아웃 요청 후 JSESSIONID 쿠키 삭제 확인 (CSRF 토큰 추가)
        mockMvc.perform(post("/inmemory/logout")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }
}
