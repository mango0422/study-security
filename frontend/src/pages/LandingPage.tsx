// src/pages/LandingPage.tsx
import { useNavigate } from 'react-router-dom'

export default function LandingPage() {
  const navigate = useNavigate()

  const goToLogin = (mode: 'inmemory' | 'jpa' | 'oauth') => {
    // 각각의 로그인 페이지 경로로 이동합니다.
    if (mode === 'inmemory') {
      navigate('/inmemory/login')
    } else if (mode === 'jpa') {
      navigate('/jpa/login')
    } else if (mode === 'oauth') {
      navigate('/oauth/login')
    }
  }

  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-gray-100">
      <h1 className="mb-6 text-4xl font-bold">Welcome to the Auth Demo</h1>
      <p className="mb-8 text-lg">아래 버튼을 눌러 원하는 인증 방식을 선택하세요.</p>
      <div className="flex flex-col space-y-4">
        <button
          onClick={() => goToLogin('inmemory')}
          className="rounded bg-blue-500 px-6 py-3 text-white hover:bg-blue-600"
        >
          InMemory 로그인
        </button>
        <button
          onClick={() => goToLogin('jpa')}
          className="rounded bg-green-500 px-6 py-3 text-white hover:bg-green-600"
        >
          JPA (Redis) 로그인
        </button>
        <button
          onClick={() => goToLogin('oauth')}
          className="rounded bg-purple-500 px-6 py-3 text-white hover:bg-purple-600"
        >
          OAuth 로그인
        </button>
      </div>
    </div>
  )
}
