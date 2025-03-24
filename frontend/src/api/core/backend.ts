// src/api/core/backend.ts
import { createAxiosInstance } from './base'
import Cookies from 'js-cookie'

const backend = createAxiosInstance('http://localhost:8080/api')

// 토큰 자동 부착
backend.interceptors.request.use(config => {
  const token = Cookies.get('accessToken')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default backend
