// src/pages/OauthCallback.tsx
import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import Cookies from 'js-cookie'

export default function OauthCallback() {
  const navigate = useNavigate()

  useEffect(() => {
    const exchangeCodeForToken = async () => {
      const code = new URLSearchParams(window.location.search).get('code')
      if (!code) {
        console.error('No authorization code found in callback URL.')
        navigate('/') // 오류 발생 시 랜딩 페이지로 이동
        return
      }

      const clientId = 'oidc-client' // 서버와 일치
      const clientSecret = 'secret' // 서버와 일치 (주의: 실제 앱에서는 노출 금지!)
      const redirectUri = 'http://localhost:5173/oauth/callback' // 서버와 일치

      // URLSearchParams를 사용하여 x-www-form-urlencoded 형식 데이터 준비
      const params = new URLSearchParams()
      params.append('grant_type', 'authorization_code')
      params.append('code', code)
      params.append('redirect_uri', redirectUri)
      // PKCE 사용 시 code_verifier 파라미터 추가 필요
      // params.append('code_verifier', 'YOUR_CODE_VERIFIER_STORED_IN_SESSION_OR_LOCALSTORAGE');

      try {
        const response = await fetch('http://localhost:9000/oauth2/token', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            Authorization: 'Basic ' + btoa(`${clientId}:${clientSecret}`), // Basic Authentication
          },
          body: params,
        })

        if (!response.ok) {
          const errorData = await response.json().catch(() => ({})) // 오류 응답 파싱 시도
          console.error('Token exchange failed:', response.status, response.statusText, errorData)
          throw new Error(`Token exchange failed: ${response.status}`)
        }

        const tokens = await response.json()
        const { access_token, refresh_token, expires_in } = tokens

        if (!access_token) {
          throw new Error('Access token not received')
        }

        // 쿠키에 토큰 저장 (js-cookie 사용)
        // 만료 시간 설정 (expires_in은 초 단위)
        const expiryDate = new Date(new Date().getTime() + expires_in * 1000)
        Cookies.set('accessToken', access_token, {
          expires: expiryDate,
          secure: true,
          sameSite: 'lax',
        }) // HTTPS에서만 전송, Lax 권장
        if (refresh_token) {
          // Refresh token은 더 긴 만료 시간 설정 가능 (예: 7일)
          Cookies.set('refreshToken', refresh_token, {
            expires: 7,
            secure: true,
            sameSite: 'strict',
          }) // Strict 권장
        }

        console.log('Tokens obtained and stored in cookies.')
        navigate('/home') // 성공 시 홈으로 이동
      } catch (err) {
        console.error('Error during token exchange:', err)
        // 사용자에게 오류 메시지 표시 또는 오류 페이지로 리디렉션
        navigate('/') // 오류 시 랜딩 페이지로 이동
      }
    }

    exchangeCodeForToken()
  }, [navigate])

  return (
    <div className="flex h-screen items-center justify-center">Processing OAuth callback...</div>
  )
}
