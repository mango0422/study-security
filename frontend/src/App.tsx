// src/App.tsx
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Nav from './components/Nav'
import { AuthProvider, useAuthContext } from './context/auth'
import { Home, Login, Signup, OauthCallback, LandingPage } from './pages'
import ProtectedRoute from './components/ProtectedRoute' // ProtectedRoute 임포트

function AppContent() {
  const { isLoading } = useAuthContext()

  if (isLoading) {
    return <div className="flex h-screen items-center justify-center">Loading...</div> // 로딩 상태 표시
  }

  return (
    <div className="flex min-h-screen flex-col">
      <Nav />
      <main className="mt-16 flex-grow">
        <Routes>
          {/* 기본 랜딩 페이지 */}
          <Route path="/" element={<LandingPage />} />

          {/* JPA/InMemory 로그인/회원가입 경로 (OAuth와 별개) */}
          <Route path="/inmemory/login" element={<Login />} />
          <Route path="/jpa/login" element={<Login />} />
          {/* 공용 회원가입 경로는 유지 */}
          <Route path="/signup" element={<Signup />} />

          {/* 보호된 경로 */}
          <Route element={<ProtectedRoute />}>
            <Route path="/home" element={<Home />} />
            {/* 다른 보호된 경로 추가 가능 */}
          </Route>

          {/* OAuth 콜백 경로 */}
          <Route path="/oauth/callback" element={<OauthCallback />} />

          {/* 다른 경로들... */}
        </Routes>
      </main>
    </div>
  )
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppContent />
      </AuthProvider>
    </BrowserRouter>
  )
}
