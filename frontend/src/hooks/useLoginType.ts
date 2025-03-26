// src/hooks/useLoginType.ts
import { useLocation } from 'react-router-dom'

export const useLoginType = (): 'JPA' | 'OAuth' | 'InMemory' => {
  const location = useLocation()
  if (location.pathname.startsWith('/jpa')) return 'JPA'
  if (location.pathname.startsWith('/oauth')) return 'OAuth'
  return 'InMemory'
}
  