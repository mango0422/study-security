package mango.security.authorizationServer.config;

// java security : 키 생성 관련련
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

// spring security : jwt 발급용용
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

// Nimbus JOSE + JWT - JWK 처리 관련
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

/**
 * JWK란?
 * - JWK (JSON Web Key)는 암호화 키를 표현하는 JSON 데이터 구조입니다.
 * - RFC 7517에 정의되어 있으며, 공개 키 혹은 비대칭/대칭 키에 대한 메타 정보를 포함할 수 있습니다.
 * - JWK는 키 타입(kty), 키 ID(kid), 공개 키 또는 비공개 키의 구성 요소(n, e, d 등)를 명시합니다.
 * - 예를 들어 RSA 키의 경우 다음 필드를 포함할 수 있습니다:
 *     - "kty": 키 타입 (예: "RSA")
 *     - "n": modulus (base64url 인코딩된 값)
 *     - "e": exponent (base64url 인코딩된 값)
 *     - "d": private exponent (optional, 서명/복호화용)
 *
 * JWK Set이란?
 * - 여러 개의 JWK를 포함하는 JSON 객체 구조입니다.
 * - "keys": [ ... ] 배열 형태로 구성됩니다.
 *
 * @Configuration
 * public class JwkConfig
 * 
 * @Bean
 * public JWKSource<SecurityContext> jwkSource()
 * - JWT 서명을 위한 JWK(RSAKey)를 생성하고, 이를 기반으로 JWKSet을 구성합니다.
 * - JWKSource는 JWT 인코더가 서명 시 사용할 키를 동적으로 선택할 수 있도록 도와줍니다.
 * - NimbusJwtEncoder와 연동되어 JWT 발급 시 서명 키로 사용됩니다.
 * 
 * private RSAKey generateRSAKey()
 * - 2048비트 RSA 키 쌍을 생성하고 이를 기반으로 Nimbus의 RSAKey 객체를 구성합니다.
 * - RSAKey.Builder를 통해 공개키/개인키, 키 ID(kid)를 설정합니다.
 * - 이렇게 생성된 RSAKey는 JWK 표준 형식으로 자동 변환됩니다.
 * 
 * @Bean
 * public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource)
 * - Spring Security의 NimbusJwtEncoder에 JWKSource를 주입하여 JWT 서명을 처리합니다.
 * - JWT를 인코딩(발급)할 때 위에서 생성된 RSA 개인 키로 서명합니다.
 * 
 * 이 구성 클래스는 OAuth2 Authorization Server에서 JWT 기반 토큰을 발급할 때,
 * 그 토큰에 서명할 RSA 키를 JWK 형식으로 관리하고 외부에 노출할 수 있도록 준비합니다.
 * 클라이언트는 `/oauth2/jwks` 또는 비슷한 경로로 JWKSet을 조회하여 토큰 검증 시 사용 가능한 공개 키를 획득할 수 있습니다.
 */


@Configuration
public class JwkConfig {

    // JWT 서명용 RSA 키를 포함한 JWKSource Bean 등록
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsakey = generateRSAKey(); // 아래에서 생성
        JWKSet jwkSet = new JWKSet(rsakey); // JWK 세트 생성
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    // RSA 키 생성 후 Nimbus RSAKey 객체로 반환
    private RSAKey generateRSAKey() {
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            return new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString())
                    .build();
        } catch (Exception ex) {
            throw new RuntimeException("RSA 키 생성 실패", ex);
        }
    }
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }
}
