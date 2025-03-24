// src/hooks/useAuth.ts
import { useLoginType } from './useLoginType'
import { jpaAuth, inMemoryAuth, oauthAuth } from '../api/auth-api'

export const useAuth = () => {
  const loginType = useLoginType()
  if (loginType === 'JPA') return jpaAuth
  if (loginType === 'OAuth') return oauthAuth
  return inMemoryAuth
}
