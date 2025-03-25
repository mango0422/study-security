// src/components/ProtectedRoute.tsx
import { Navigate, Outlet } from 'react-router-dom'
import { useAuthContext } from '../context/auth' // useAuthContext 사용

export default function ProtectedRoute() {
  const { isAuthenticated } = useAuthContext() // AuthContext에서 상태 가져오기

  // console.log('ProtectedRoute check:', isAuthenticated); // 디버깅용

  return isAuthenticated ? <Outlet /> : <Navigate to="/" replace />
}
