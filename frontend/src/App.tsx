// src/App.tsx
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Nav from './components/Nav'
import { AuthProvider } from './context/auth'
import { Home, Login, Signup, OauthCallback, LandingPage } from './pages'

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <div className="flex min-h-screen flex-col">
          <Nav />
          <main className="mt-16 flex-grow">
            <Routes>
              {/* 기본 랜딩 페이지 */}
              <Route path="/" element={<LandingPage />} />
              {/* 각 인증 방식별 로그인 경로 */}
              <Route path="/inmemory/login" element={<Login />} />
              <Route path="/jpa/login" element={<Login />} />
              <Route path="/oauth/login" element={<Login />} />
              <Route path="/signup" element={<Signup />} />
              <Route path="/home" element={<Home />} />
              <Route path="/oauth/callback" element={<OauthCallback />} />
            </Routes>
          </main>
        </div>
      </AuthProvider>
    </BrowserRouter>
  )
}
