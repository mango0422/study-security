import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'

export default function OauthCallback() {
  const navigate = useNavigate()

  useEffect(() => {
    const exchangeCodeForToken = async () => {
      const code = new URLSearchParams(window.location.search).get('code')
      if (!code) {
        console.error('No code in callback URL')
        return
      }

      try {
        const response = await axios.post('http://localhost:9000/oauth2/token', {
          grant_type: 'authorization_code',
          code,
          redirect_uri: 'http://localhost:5173/oauth/callback',
          client_id: 'YOUR_CLIENT_ID',
          client_secret: 'YOUR_CLIENT_SECRET', // 필요 시
        })

        const { access_token, refresh_token } = response.data
        localStorage.setItem('accessToken', access_token)
        localStorage.setItem('refreshToken', refresh_token)

        navigate('/home')
      } catch (err) {
        console.error('Token exchange failed', err)
      }
    }

    exchangeCodeForToken()
  }, [navigate])

  return <div>OAuth 처리 중입니다...</div>
}
