// src/pages/LandingPage.tsx
import { useNavigate } from 'react-router-dom'
import { oauthAuth } from '../api/auth-api' // oauthAuth 임포트

export default function LandingPage() {
  const navigate = useNavigate()

  const goToLogin = (mode: 'inmemory' | 'jpa') => {
    // oauth 제외
    if (mode === 'inmemory') {
      navigate('/inmemory/login')
    } else if (mode === 'jpa') {
      navigate('/jpa/login')
    }
  }

  const handleOAuthLogin = () => {
    oauthAuth.login() // 바로 리디렉션 시작
  }

  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-gray-100">
      <h1 className="mb-6 text-4xl font-bold">Welcome to the Auth Demo</h1>
      <p className="mb-8 text-lg">아래 버튼을 눌러 원하는 인증 방식을 선택하세요.</p>
      <div className="flex flex-col space-y-4">
        {/* ... InMemory, JPA 버튼은 그대로 ... */}
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
        {/* OAuth 로그인 버튼 수정 */}
        <button
          onClick={handleOAuthLogin} // oauthAuth.login 호출
          className="rounded bg-purple-500 px-6 py-3 text-white hover:bg-purple-600"
        >
          OAuth 로그인
        </button>
      </div>
    </div>
  )
}
