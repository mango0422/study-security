// src/api/auth.js
const API_BASE_URL = "http://localhost:8080";

export const authApi = {
  login: async (endpoint, credentials) => {
    try {
      const response = await fetch(`${API_BASE_URL}/${endpoint}/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(credentials),
        credentials: "include", // 쿠키 기반 세션을 위한 설정
      });

      const data = await response.json();
      return {
        success: response.ok,
        data,
        message: response.ok ? "로그인 성공!" : `로그인 실패: ${data.message}`,
      };
    } catch (error) {
      return {
        success: false,
        data: null,
        message: `로그인 중 오류가 발생했습니다: ${error.message}`,
      };
    }
  },

  // 추후 필요한 다른 인증 관련 API 함수들 추가 가능
  logout: async () => {
    // 로그아웃 로직
  },

  register: async (userData) => {
    // 회원가입 로직
  },

  checkAuth: async () => {
    // 인증 상태 확인 로직
  },
};
