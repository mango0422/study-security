// src/context/auth.tsx
import { createContext, useContext, useEffect, useState } from 'react'
import { useLoginType } from '../hooks/useLoginType'
import { jpaAuth, inMemoryAuth, oauthAuth } from '../api/auth-api'

interface AuthContextType {
  isAuthenticated: boolean
  login: (email: string, password: string) => Promise<void>
  signup: (email: string, password: string) => Promise<void>
  logout: () => Promise<void>
}

const AuthContext = createContext<AuthContextType>({
  isAuthenticated: false,
  login: async () => {},
  signup: async () => {},
  logout: async () => {},
})

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const loginType = useLoginType()
  const authApi = loginType === 'JPA' ? jpaAuth : loginType === 'OAuth' ? oauthAuth : inMemoryAuth

  // 인증 상태 확인용 check 엔드포인트가 있다면 사용 (추가 구현)
  useEffect(() => {
    // 예: authApi.check && authApi.check().then(() => setIsAuthenticated(true)).catch(() => setIsAuthenticated(false))
  }, [authApi])

  const login = async (email: string, password: string) => {
    await authApi.login(email, password)
    setIsAuthenticated(true)
  }

  const signup = async (email: string, password: string) => {
    await authApi.signup(email, password)
    setIsAuthenticated(true)
  }

  const logout = async () => {
    await authApi.logout()
    setIsAuthenticated(false)
  }

  return (
    <AuthContext.Provider value={{ isAuthenticated, login, signup, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuthContext = () => useContext(AuthContext)
