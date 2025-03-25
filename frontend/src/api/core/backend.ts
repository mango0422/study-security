// src/api/core/backend.ts
import axios from 'axios' // axios 직접 사용으로 변경 (createAxiosInstance 불필요 시)
import Cookies from 'js-cookie'
import { oauthAuth } from '../auth-api' // oauthAuth 임포트

const backend = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  withCredentials: true, // 쿠키 전송에 필요할 수 있음
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request 인터셉터: 요청 전에 Access Token 부착
backend.interceptors.request.use(
  config => {
    const token = Cookies.get('accessToken')
    if (token && !config.headers.Authorization) {
      // Authorization 헤더가 없을 때만 추가
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// Response 인터셉터: 401 에러 시 토큰 재발급 및 재시도
let isRefreshing = false // 재발급 중복 방지 플래그
let failedQueue: { resolve: (value: unknown) => void; reject: (reason?: any) => void; config: any } // 실패한 요청 큐

const processQueue = (error: any, token: string | null = null) => {
  failedQueue.forEach(prom => {
    if (error) {
      prom.reject(error)
    } else {
      prom.config.headers['Authorization'] = 'Bearer ' + token
      prom.resolve(backend(prom.config)) // axios 인스턴스로 재요청
    }
  })
  failedQueue
}

backend.interceptors.response.use(
  response => response, // 성공 응답은 그대로 반환
  async error => {
    const originalRequest = error.config

    // 401 에러이고, 재시도 요청이 아니며, refresh token이 있을 경우
    if (error.response?.status === 401 && !originalRequest._retry && Cookies.get('refreshToken')) {
      if (isRefreshing) {
        // 이미 재발급 중이면 큐에 추가
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject, config: originalRequest })
        })
      }

      originalRequest._retry = true // 재시도 플래그 설정
      isRefreshing = true

      const refreshTokenValue = Cookies.get('refreshToken')

      try {
        if (!refreshTokenValue) throw new Error('No refresh token found')

        const { access_token: newAccessToken, refresh_token: newRefreshToken } =
          await oauthAuth.refreshToken(refreshTokenValue)

        // 새 Access Token 쿠키 저장 (만료 시간은 서버 응답에 따라 설정해야 하나, 여기서는 간단히 설정)
        // 실제로는 expires_in 값을 받아 계산해야 함
        Cookies.set('accessToken', newAccessToken, { secure: true, sameSite: 'lax' })
        if (newRefreshToken) {
          // 새 Refresh Token이 발급된 경우 업데이트
          Cookies.set('refreshToken', newRefreshToken, {
            expires: 7,
            secure: true,
            sameSite: 'strict',
          })
        }

        console.log('Token refreshed successfully.')
        backend.defaults.headers.common['Authorization'] = 'Bearer ' + newAccessToken // axios 인스턴스 기본 헤더 업데이트
        originalRequest.headers['Authorization'] = 'Bearer ' + newAccessToken // 현재 요청 헤더 업데이트

        processQueue(null, newAccessToken) // 큐에 있던 요청들 처리
        return backend(originalRequest) // 원래 요청 재시도
      } catch (refreshError) {
        console.error('Unable to refresh token:', refreshError)
        // Refresh 실패 시 로그아웃 처리
        Cookies.remove('accessToken')
        Cookies.remove('refreshToken')
        processQueue(refreshError, null) // 큐에 있던 요청들 실패 처리
        // 로그인 페이지로 리디렉션 또는 에러 상태 관리
        // window.location.href = '/'; // 예시: 랜딩 페이지로 강제 이동
        return Promise.reject(refreshError)
      } finally {
        isRefreshing = false
      }
    }

    return Promise.reject(error) // 그 외 에러는 그대로 반환
  }
)

export default backend
