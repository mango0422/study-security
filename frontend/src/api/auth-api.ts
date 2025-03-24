// src/api/auth-api.ts
import { createHttpClient, backend, authServer } from './core'

const backendHttp = createHttpClient(backend)
const oauthHttp = createHttpClient(authServer)

interface AuthResponse {
  accessToken: string
  refreshToken: string
}

// JPA 기반 인증 API 모듈
export const jpaAuth = {
  login: (email: string, password: string) =>
    backendHttp.post<AuthResponse>({ url: '/auth/jpa/login', data: { email, password } }),
  signup: (email: string, password: string) =>
    backendHttp.post<AuthResponse>({
      url: '/auth/jpa/signup',
      data: { email, password, loginType: 'JPA' },
    }),
  logout: () => backendHttp.delete({ url: '/auth/jpa/logout' }),
  // check 엔드포인트 추가 가능 시 여기에...
}

// InMemory 기반 인증 API 모듈
export const inMemoryAuth = {
  login: (email: string, password: string) =>
    backendHttp.post<AuthResponse>({ url: '/auth/inmemory/login', data: { email, password } }),
  signup: (email: string, password: string) =>
    backendHttp.post<AuthResponse>({
      url: '/auth/inmemory/signup',
      data: { email, password, loginType: 'InMemory' },
    }),
  logout: () => backendHttp.delete({ url: '/auth/inmemory/logout' }),
  // check 엔드포인트 추가 가능 시...
}

// OAuth 관련 (기존 그대로)
export const oauthAuth = {
  login: () => {
    window.location.href =
      'http://localhost:9000/oauth2/authorize?client_id=YOUR_CLIENT_ID&redirect_uri=http://localhost:5173/oauth/callback&response_type=code'
  },
  signup: () => {
    window.location.href =
      'http://localhost:9000/oauth2/authorize?client_id=YOUR_CLIENT_ID&redirect_uri=http://localhost:5173/oauth/callback&response_type=code'
  },
  logout: () =>
    oauthHttp.post({
      url: '/api/auth/oauth/logout',
    }),
}
