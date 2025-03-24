package mango.security.basic_login;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BasicLoginApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(BasicLoginApplicationTests.class);

	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {
		logger.info("애플리케이션 컨텍스트가 정상적으로 로딩되었습니다.");

		// 학습용으로 중요한 빈들 나열 및 설명

		// 1. BasicLoginApplication
		logger.info("BasicLoginApplication: 스프링 부트 애플리케이션의 엔트리 포인트입니다.");

		// 2. InMemorySecurityConfig
		if (context.containsBean("inMemorySecurityConfig")) {
			logger.info("InMemorySecurityConfig: 인메모리 인증 방식을 위한 보안 설정 빈입니다.");
		} else {
			logger.error("InMemorySecurityConfig 빈이 생성되지 않았습니다!");
		}

		// 3. JpaSecurityConfig
		if (context.containsBean("jpaSecurityConfig")) {
			logger.info("JpaSecurityConfig: JPA 기반 인증을 위한 보안 설정 빈입니다.");
		} else {
			logger.error("JpaSecurityConfig 빈이 생성되지 않았습니다!");
		}

		// 4. InMemoryUserDetailsService
		if (context.containsBean("inMemoryUserDetailsService")) {
			logger.info("InMemoryUserDetailsService: 인메모리 사용자 정보를 관리하는 서비스 빈입니다.");
		} else {
			logger.error("InMemoryUserDetailsService 빈이 생성되지 않았습니다!");
		}

		// 5. JpaUserDetailsService
		if (context.containsBean("jpaUserDetailsService")) {
			logger.info("JpaUserDetailsService: JPA를 통해 DB에 저장된 사용자 정보를 관리하는 서비스 빈입니다.");
		} else {
			logger.error("JpaUserDetailsService 빈이 생성되지 않았습니다!");
		}

		// 6. MemberRepository
		if (context.containsBean("memberRepository")) {
			logger.info("MemberRepository: Member 엔티티를 위한 JPA Repository 빈입니다.");
		} else {
			logger.error("MemberRepository 빈이 생성되지 않았습니다!");
		}

		// 7. PasswordEncoder
		if (context.containsBean("passwordEncoder")) {
			logger.info("PasswordEncoder: 비밀번호 암호화를 담당하는 BCryptPasswordEncoder 빈입니다.");
		} else {
			logger.error("PasswordEncoder 빈이 생성되지 않았습니다!");
		}

		// 8. InMemoryUserDetailsManager (InMemorySecurityConfig 내에서 생성)
		if (context.containsBean("inMemoryUserDetailsManager")) {
			logger.info("InMemoryUserDetailsManager: 인메모리 사용자 인증 정보를 관리하는 빈입니다.");
		} else {
			logger.error("InMemoryUserDetailsManager 빈이 생성되지 않았습니다!");
		}

		// 필요한 경우 추가적인 빈들을 로그로 출력할 수 있습니다.
	}
}
