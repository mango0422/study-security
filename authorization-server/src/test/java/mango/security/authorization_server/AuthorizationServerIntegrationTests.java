package mango.security.authorization_server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.test.web.servlet.MockMvc;

import mango.security.authorizationServer.AuthorizationServerApplication;

@SpringBootTest(classes = AuthorizationServerApplication.class)
@AutoConfigureMockMvc
class AuthorizationServerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    /**
     * 클라이언트 등록 확인 테스트
     */
    @Test
    void testClientRegistration() {
        RegisteredClient client = registeredClientRepository.findByClientId("mango-client");
        assertNotNull(client, "mango-client가 등록되어 있어야 합니다.");
        assertThat(client.getClientId()).isEqualTo("mango-client");
    }

    /**
     * /oauth2/jwks 엔드포인트가 정상적으로 JWT 공개키 정보를 반환하는지 테스트
     */
    @Test
    void testJwksEndpoint() throws Exception {
        mockMvc.perform(get("/oauth2/jwks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    /**
     * 기본 form 로그인 테스트
     * (Spring Boot 기본 사용자(user/password)는 테스트 실행 시 자동 생성됩니다.)
     */
    @Test
    void testFormLogin() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "user")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection());
    }

    /**
     * OAuth2 토큰 발급 테스트 (authorization_code grant)
     *
     * 주의: 이 테스트는 실제 authorization code 발급 과정을 완전히 재현하지는 않으므로,
     * 테스트용으로 dummy code를 전달하여 엔드포인트가 호출되는지와 JSON 응답 구조를 확인합니다.
     * 실제 정상 동작을 위해서는 올바른 authorization code와 인증 세션이 필요합니다.
     */
    @Test
    void testTokenIssuance() throws Exception {
        mockMvc.perform(post("/oauth2/token")
                .param("grant_type", "authorization_code")
                .param("client_id", "mango-client")
                .param("client_secret", "mango-secret")
                .param("code", "dummyAuthorizationCode")
                .param("redirect_uri", "http://localhost:3000/login/oauth2/code/mango-client"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
}
