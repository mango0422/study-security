// src/api/auth-api.ts
import { createHttpClient, backend /*, authServer */ } from './core'
import Cookies from 'js-cookie'

// Axios 인스턴스 생성 (기존 코드 활용)
const backendHttp = createHttpClient(backend)
// authServer 인스턴스는 /oauth2/token 엔드포인트 호출 시 Content-Type이 다르므로
// OauthCallback에서 직접 axios를 사용하거나 별도 설정된 인스턴스를 사용합니다.
// const oauthHttp = createHttpClient(authServer)

interface AuthResponse {
  // JPA/InMemory용 인터페이스 (OAuth에서는 직접 사용 안 함)
  accessToken: string
  refreshToken: string
}

// JPA 기반 인증 API 모듈 (OAuth와 별개)
export const jpaAuth = {
  login: (email: string, password: string) =>
    backendHttp.post<AuthResponse>({ url: '/auth/jpa/login', data: { email, password } }),
  // ... (signup, logout 등)
}

// InMemory 기반 인증 API 모듈 (OAuth와 별개)
export const inMemoryAuth = {
  login: (email: string, password: string) =>
    backendHttp.post<AuthResponse>({ url: '/auth/inmemory/login', data: { email, password } }),
  // ... (signup, logout 등)
}

// OAuth 관련 API
export const oauthAuth = {
  // 로그인 시작: Authorization Server로 리디렉션
  login: () => {
    // !! 중요: client_id와 redirect_uri를 서버 설정과 일치시킵니다.
    const clientId = 'oidc-client'
    const redirectUri = 'http://localhost:5173/oauth/callback'
    const authorizeUrl = `http://localhost:9000/oauth2/authorize?response_type=code&client_id=${clientId}&scope=openid%20profile%20message.read&redirect_uri=${redirectUri}`
    // TODO: PKCE 파라미터(code_challenge, code_challenge_method) 추가 권장

    window.location.href = authorizeUrl
  },
  // 회원가입도 동일한 OAuth 흐름을 따릅니다 (Auth Server에서 처리).
  signup: () => {
    oauthAuth.login()
  },
  // 로그아웃: 클라이언트 측 토큰 제거 및 상태 초기화
  logout: async () => {
    // 실제로는 Authorization Server의 로그아웃 엔드포인트 호출/리디렉션 필요할 수 있음
    // 예: window.location.href = 'http://localhost:9000/logout?id_token_hint=...&post_logout_redirect_uri=...'
    Cookies.remove('accessToken')
    Cookies.remove('refreshToken')
    // TODO: 서버 세션 무효화 또는 토큰 폐기 API 호출 (필요시)
    console.log('OAuth logout initiated (client-side token removal)')
    return Promise.resolve()
  },
  // 토큰 새로고침 함수 (backend 인터셉터 내부에서 사용됨)
  refreshToken: async (
    refreshTokenValue: string
  ): Promise<{ access_token: string; refresh_token?: string }> => {
    const clientId = 'oidc-client'
    const clientSecret = 'secret' // 실제 앱에서는 노출되면 안됨!

    const params = new URLSearchParams()
    params.append('grant_type', 'refresh_token')
    params.append('refresh_token', refreshTokenValue)

    const response = await fetch('http://localhost:9000/oauth2/token', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        Authorization: 'Basic ' + btoa(`${clientId}:${clientSecret}`), // Basic Auth
      },
      body: params,
    })

    if (!response.ok) {
      throw new Error(`Token refresh failed: ${response.statusText}`)
    }

    return response.json()
  },
}
