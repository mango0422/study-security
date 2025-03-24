# Basic Login 프로젝트

이 프로젝트는 스프링 부트를 활용하여 기본 로그인 기능을 학습하기 위한 예제입니다. InMemory 방식과 JPA 기반의 사용자 인증 두 가지 방법을 비교할 수 있도록 구성되어 있습니다. 프로젝트의 구성 요소와 실행 방법, 보안 설정 등을 자세히 설명하여, 사용자가 실습하며 내부 동작 원리를 온전히 이해할 수 있도록 돕습니다.

---

## 목차

- [프로젝트 개요](#프로젝트-개요)
- [주요 기능](#주요-기능)
- [프로젝트 구조](#프로젝트-구조)
- [설정 및 의존성](#설정-및-의존성)
- [보안 설정](#보안-설정)
    - [InMemory 방식](#inmemory-방식)
    - [JPA 방식](#jpa-방식)
- [템플릿 (Thymeleaf) 구성](#템플릿-thymeleaf-구성)
- [실행 방법](#실행-방법)
- [학습 포인트](#학습-포인트)
- [추가 참고 사항](#추가-참고-사항)

---

## 프로젝트 개요

이 프로젝트는 두 가지 인증 방식을 제공하는 스프링 부트 애플리케이션입니다.

- **InMemory 인증**  
  인메모리 방식으로 초기 사용자 데이터를 등록하여 로그인 및 회원가입 기능을 수행합니다. 간단한 사용자 관리와 인증 처리를 이해하는 데 유용합니다.

- **JPA 인증**  
  JPA를 활용하여 데이터베이스에 저장된 사용자 정보를 기반으로 인증을 수행합니다. PostgreSQL을 데이터베이스로 사용하며, 회원가입 시 저장되는 사용자 정보는 `Member` 엔티티에 매핑됩니다.

두 방식은 각기 다른 URL 패턴 (`/inmemory/**`, `/jpa/**`)으로 구분되며, 각각의 로그인, 로그아웃, 회원가입 및 홈 페이지를 제공합니다.

---

## 주요 기능

- **사용자 로그인**  
  각 방식별로 커스텀 로그인 페이지를 제공하며, 인증 후 홈 페이지로 리다이렉트 됩니다.

- **회원가입**  
  사용자 이름(이메일 형식)과 패스워드를 입력받아 InMemory 또는 JPA 방식으로 회원가입을 지원합니다.

- **로그아웃**  
  로그아웃 시 JSESSIONID 쿠키를 제거하여 세션을 무효화합니다.

- **다크모드 지원**  
  CSS 미디어쿼리(`prefers-color-scheme`)를 활용하여 자동 다크모드 전환을 지원합니다.

---

## 프로젝트 구조

프로젝트는 아래와 같은 디렉토리 구조로 구성되어 있습니다.

```
basic-login/
├── src
│   ├── main
│   │   ├── java
│   │   │   └── mango/security/basic_login/
│   │   │       ├── BasicLoginApplication.java         // 애플리케이션 엔트리 포인트
│   │   │       ├── config/
│   │   │       │   ├── InMemorySecurityConfig.java      // 인메모리 보안 설정
│   │   │       │   └── JpaSecurityConfig.java           // JPA 기반 보안 설정
│   │   │       ├── controller/
│   │   │       │   ├── InMemoryController.java          // 인메모리 관련 컨트롤러
│   │   │       │   └── JpaController.java               // JPA 관련 컨트롤러
│   │   │       ├── entity/
│   │   │       │   └── Member.java                      // 사용자 엔티티 (JPA)
│   │   │       ├── repository/
│   │   │       │   └── MemberRepository.java            // JPA Repository
│   │   │       ├── service/
│   │   │       │   ├── InMemoryUserDetailsService.java  // 인메모리 사용자 서비스
│   │   │       │   └── JpaUserDetailsService.java       // JPA 사용자 서비스
│   │   │       └── type/
│   │   │           └── Role.java                        // 사용자 역할 정의 (USER, ADMIN)
│   │   └── resources
│   │       ├── templates/
│   │       │   ├── home.html                          // 홈 페이지
│   │       │   ├── login.html                         // 로그인 페이지
│   │       │   ├── logout.html                        // 로그아웃 안내 페이지
│   │       │   └── signup.html                        // 회원가입 페이지
│   │       └── application.yml                        // 애플리케이션 설정 파일 (데이터베이스, JPA, Redis 등)
├── build.gradle                                        // 빌드 스크립트
└── README.md                                           // 이 파일
```

---

## 설정 및 의존성

- **Spring Boot Starter**
    - `spring-boot-starter-web` : 웹 애플리케이션 개발 지원
    - `spring-boot-starter-data-jpa` : JPA 기반 데이터 접근
    - `spring-boot-starter-security` : 스프링 시큐리티 지원
    - `spring-boot-starter-thymeleaf` : 템플릿 엔진
    - `spring-boot-starter-validation` : 데이터 검증 기능

- **데이터베이스 및 드라이버**
    - PostgreSQL 데이터베이스 사용 (`postgresql` 드라이버)

- **Lombok**
    - 코드 간결화를 위해 Lombok을 사용

- **Redis (옵션)**
    - `spring.data.redis` 설정이 포함되어 있으므로, Redis를 통한 캐싱 또는 세션 관리를 위한 설정도 가능합니다.

- **Gradle 빌드 도구**
    - `build.gradle` 파일에서 관련 플러그인과 의존성 관리

설정 파일 (`application.yml`)에서는 데이터베이스 연결 정보와 JPA 관련 속성이 정의되어 있습니다. 환경 변수(`POSTGRES_HOST`, `POSTGRES_PORT` 등)를 사용하여 유연하게 설정할 수 있도록 구성되어 있습니다.

---

## 보안 설정

프로젝트에서는 두 가지 방식의 사용자 인증을 제공합니다. 각 방식별 보안 설정은 다음과 같이 구성됩니다.

### InMemory 방식

- **설정 파일:** `InMemorySecurityConfig.java`
- **주요 기능:**
    - URL 패턴: `/inmemory/**`
    - 로그인 페이지: `/inmemory/login`
    - 회원가입 페이지: `/inmemory/signup`
    - 홈 페이지: `/inmemory/home` (인증 후 접근 가능)
    - 로그인 성공 시 기본적으로 `/inmemory/home`로 리다이렉트
    - 로그아웃 시 JSESSIONID 쿠키를 수동으로 제거 후 `/inmemory/logout?success`로 리다이렉트
- **사용자 관리:**
    - `InMemoryUserDetailsManager`를 사용하여 사용자 정보(예: user, admin)를 메모리 상에 등록
    - 추가 사용자 등록은 `InMemoryUserDetailsService`를 통해 수행

### JPA 방식

- **설정 파일:** `JpaSecurityConfig.java`
- **주요 기능:**
    - URL 패턴: `/jpa/**`
    - 로그인 페이지: `/jpa/login`
    - 회원가입 페이지: `/jpa/signup`
    - 홈 페이지: `/jpa/home` (인증 후 접근 가능)
    - 로그인 성공 시 기본적으로 `/jpa/home`로 리다이렉트
    - 로그아웃 시 JSESSIONID 쿠키를 수동으로 제거 후 `/jpa/logout?success`로 리다이렉트
- **사용자 관리:**
    - `JpaUserDetailsService`를 통해 데이터베이스(`Member` 엔티티) 기반으로 사용자 정보 관리
    - 회원가입 시, 이메일과 암호를 입력받아 사용자를 DB에 저장하며, 비밀번호는 BCrypt 암호화 처리

---

## 템플릿 (Thymeleaf) 구성

프로젝트는 Thymeleaf 템플릿 엔진을 사용하여 뷰를 렌더링 합니다. 각 페이지는 다음과 같은 특징을 가집니다.

- **login.html**
    - 사용자에게 로그인 폼 제공
    - `loginType` 변수에 따라 `/jpa/process_login` 또는 `/inmemory/process_login` URL로 폼 전송
    - 로그인 실패 또는 로그아웃 메시지 출력

- **signup.html**
    - 사용자에게 회원가입 폼 제공
    - 이메일 형식의 username 입력 필드와 password 입력 필드 포함
    - `loginType` 변수에 따라 회원가입 처리를 분기 (JPA 또는 InMemory)

- **home.html**
    - 로그인 성공 후 보여지는 홈 페이지
    - `loginType`에 따라 로그아웃 링크가 `/jpa/logout` 또는 `/inmemory/logout`으로 분기

- **logout.html**
    - 로그아웃 후 안내 메시지 및 재로그인 링크 제공

또한, 각 HTML 파일은 CSS 미디어쿼리를 이용한 다크모드 지원을 포함하여, 사용자의 시스템 테마에 따라 자동으로 스타일이 전환됩니다.

---

## 실행 방법

1. **환경 변수 설정**  
   `application.yml` 파일에서 사용하는 환경 변수 (`POSTGRES_HOST`, `POSTGRES_PORT`, `POSTGRES_DATABASE`, `POSTGRES_USER`, `POSTGRES_PASSWORD`, `REDIS_HOST`, `REDIS_PORT`)를 설정합니다. 또는 `.env.properties` 파일을 사용하여 환경변수를 관리할 수 있습니다.

2. **데이터베이스 준비**  
   PostgreSQL 데이터베이스가 설치되어 있고, 위 환경 변수에 맞게 데이터베이스 및 사용자 권한이 구성되어 있어야 합니다.

3. **프로젝트 빌드**  
   Gradle을 사용하여 프로젝트를 빌드합니다.
   ```bash
   ./gradlew build
   ```

4. **애플리케이션 실행**  
   빌드 후 아래 명령어로 애플리케이션을 실행합니다.
   ```bash
   ./gradlew bootRun
   ```
   또는 생성된 JAR 파일을 실행합니다.
   ```bash
   java -jar build/libs/basic-login-0.0.1-SNAPSHOT.jar
   ```

5. **접속 URL**
    - 인메모리 로그인: [http://localhost:8080/inmemory/login](http://localhost:8080/inmemory/login)
    - JPA 로그인: [http://localhost:8080/jpa/login](http://localhost:8080/jpa/login)

---

## 학습 포인트

- **스프링 부트 시큐리티 이해**  
  두 가지 방식의 사용자 인증 (메모리 기반 vs 데이터베이스 기반)을 비교하고, 각각의 보안 설정을 직접 구현해 봄으로써 스프링 시큐리티의 동작 원리를 파악할 수 있습니다.

- **커스텀 로그인 페이지 구성**  
  Thymeleaf 템플릿 엔진을 활용한 커스텀 로그인, 회원가입, 로그아웃 페이지 구성 및 다크모드 지원 CSS 작성법 학습

- **세션 관리**  
  로그아웃 처리 시 JSESSIONID 쿠키를 직접 제거하는 방법을 이해할 수 있습니다.

- **비밀번호 암호화**  
  BCryptPasswordEncoder를 사용하여 안전하게 비밀번호를 암호화하는 방법을 실습할 수 있습니다.

- **JPA를 이용한 데이터 영속성**  
  JPA 엔티티와 Repository를 활용하여 사용자 정보를 데이터베이스에 저장하고, 이를 기반으로 인증 처리를 수행하는 방법을 학습합니다.

- **환경 변수 활용**  
  `application.yml` 파일에서 환경 변수를 동적으로 불러오는 방식으로, 실제 운영환경에서 설정 파일 관리 및 보안 관련 고려 사항을 이해할 수 있습니다.

---

## 추가 참고 사항

- **예외 처리**  
  회원가입 시 이미 존재하는 사용자에 대한 예외 처리와 같은 간단한 비즈니스 로직이 포함되어 있으므로, 실제 서비스에서는 보다 정교한 예외 처리 및 로깅이 필요합니다.

- **확장 가능성**  
  인메모리 방식은 학습 및 테스트 목적으로 적합하며, JPA 방식을 기반으로 실제 운영환경에 맞게 사용자 관리 로직을 확장할 수 있습니다.

- **테스트 코드**  
  현재 예제에서는 기본적인 기능만 구현되어 있으므로, 추가적으로 스프링 시큐리티 테스트나 JUnit 기반의 단위/통합 테스트를 작성하여 보안 관련 동작을 검증해 보는 것이 좋습니다.

---

위 내용을 따라 프로젝트 구조와 설정, 보안 구성에 대해 충분히 이해한 후 실습하면, 스프링 부트를 활용한 기본 로그인 구현 및 확장에 대한 좋은 학습 자료가 될 것입니다.

Happy Coding!