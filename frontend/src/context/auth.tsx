// src/context/auth.tsx
import React, { createContext, useContext, useEffect, useState, useCallback } from 'react'
import Cookies from 'js-cookie'
import { useLoginType } from '../hooks/useLoginType'
import { jpaAuth, inMemoryAuth, oauthAuth } from '../api/auth-api'
import backend from '../api/core/backend' // backend 인스턴스 직접 임포트

interface AuthContextType {
  isAuthenticated: boolean
  checkAuth: () => Promise<void> // 인증 상태 확인 함수 추가
  login: (email: string, password: string) => Promise<void> // JPA/InMemory 용
  signup: (email: string, password: string) => Promise<void> // JPA/InMemory 용
  logout: () => Promise<void>
  isLoading: boolean // 로딩 상태 추가
}

const AuthContext = createContext<AuthContextType>({
  isAuthenticated: false,
  checkAuth: async () => {},
  login: async () => {},
  signup: async () => {},
  logout: async () => {},
  isLoading: true, // 초기 로딩 상태 true
})

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const [isLoading, setIsLoading] = useState(true) // 로딩 상태
  const loginType = useLoginType() // 현재 경로 기반 로그인 타입

  // 현재 타입에 맞는 API 모듈 선택 (JPA/InMemory는 예시)
  const getAuthApi = useCallback(
    () => {
      // 현재 프로젝트에서는 OAuth만 사용하므로 oauthAuth만 고려
      // 실제로는 loginType에 따라 분기
      return oauthAuth
      // if (loginType === 'JPA') return jpaAuth;
      // if (loginType === 'OAuth') return oauthAuth;
      // return inMemoryAuth;
    },
    [
      /* loginType */
    ]
  ) // loginType이 변경될 때마다 API 객체 다시 가져옴

  // 인증 상태 확인 로직
  const checkAuth = useCallback(async () => {
    setIsLoading(true)
    const token = Cookies.get('accessToken')
    if (token) {
      try {
        await backend.get('/messages')
        setIsAuthenticated(true)
        console.log('✅ User is authenticated via token check.')
      } catch (error) {
        console.error('❌ Token validation failed:', error)
        setIsAuthenticated(false)
      }
    } else {
      console.log('❌ No access token found.')
      setIsAuthenticated(false)
    }
    setIsLoading(false)
  }, [])

  useEffect(() => {
    checkAuth()
  }, [checkAuth]) // 이제 checkAuth는 변하지 않으므로 useEffect는 앱 로드시 한 번만 실행됩니다.

  // 앱 로드 시 인증 상태 확인
  useEffect(() => {
    checkAuth()
  }, [checkAuth])

  // JPA/InMemory 로그인 (OAuth와는 별개)
  const login = async (email: string, password: string) => {
    // const api = getAuthApi();
    // if (api === oauthAuth) {
    //   console.warn("OAuth login is initiated via redirect, not this function.");
    //   return;
    // }
    // await api.login(email, password); // JPA 또는 InMemory API 호출
    // await checkAuth(); // 로그인 성공 후 상태 재확인
    console.warn('JPA/InMemory login not implemented in this context')
  }

  // JPA/InMemory 회원가입 (OAuth와는 별개)
  const signup = async (email: string, password: string) => {
    // const api = getAuthApi();
    // if (api === oauthAuth) {
    //    console.warn("OAuth signup is handled by the Authorization Server.");
    //    return;
    // }
    // await api.signup(email, password); // JPA 또는 InMemory API 호출
    // await checkAuth(); // 가입 성공 후 상태 재확인 (자동 로그인 시)
    console.warn('JPA/InMemory signup not implemented in this context')
  }

  // 로그아웃
  const logout = async () => {
    const api = getAuthApi()
    try {
      await api.logout() // API 호출 (클라이언트 측 토큰 제거 포함)
    } catch (error) {
      console.error('Logout API call failed:', error)
      // API 호출 실패해도 클라이언트 측 토큰은 제거
      Cookies.remove('accessToken')
      Cookies.remove('refreshToken')
    } finally {
      setIsAuthenticated(false) // 상태 업데이트
    }
  }

  return (
    <AuthContext.Provider value={{ isAuthenticated, checkAuth, login, signup, logout, isLoading }}>
      {children}
    </AuthContext.Provider>
  )
}

// useAuthContext 훅 이름 변경 방지 위해 그대로 둠
export const useAuthContext = () => useContext(AuthContext)

// useAuth 훅은 API 객체 직접 반환하므로 이름 유지
export const useAuth = () => {
  const loginType = useLoginType()
  if (loginType === 'JPA') return jpaAuth
  if (loginType === 'OAuth') return oauthAuth
  return inMemoryAuth
}
