// src/components/Login.jsx
import { useState } from "react";
import { authApi } from "./api/auth";

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [activeEndpoint, setActiveEndpoint] = useState(null);

  const handleLogin = async (endpoint) => {
    setIsLoading(true);
    setActiveEndpoint(endpoint);
    setMessage("");

    try {
      const result = await authApi.login(endpoint, { username, password });
      setMessage(result.message);

      if (result.success) {
        console.log("로그인 성공:", result.data);
      }
    } catch (error) {
      setMessage(`❌ 예상치 못한 오류: ${error.message}`);
    } finally {
      setIsLoading(false);
      setTimeout(() => setActiveEndpoint(null), 300);
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-[var(--bg-color)] py-12 px-4 sm:px-6 lg:px-8">
      <div className="w-full max-w-md space-y-8">
        <div>
          <h2 className="mt-6 text-center text-3xl font-bold tracking-tight text-[var(--text-color)]">
            로그인
          </h2>
        </div>
        <div className="mt-8 space-y-6 bg-[var(--input-bg)] p-8 shadow-lg rounded-lg">
          <div className="space-y-4">
            <div>
              <label htmlFor="username" className="sr-only">
                아이디
              </label>
              <input
                id="username"
                type="text"
                required
                className="relative block w-full appearance-none rounded-md border px-3 py-2 text-[var(--text-color)] placeholder-gray-500 focus:z-10 focus:outline-none focus:ring-2 focus:ring-[var(--button-primary)]"
                placeholder="아이디"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
            </div>
            <div>
              <label htmlFor="password" className="sr-only">
                비밀번호
              </label>
              <input
                id="password"
                type="password"
                required
                className="relative block w-full appearance-none rounded-md border px-3 py-2 text-[var(--text-color)] placeholder-gray-500 focus:z-10 focus:outline-none focus:ring-2 focus:ring-[var(--button-primary)]"
                placeholder="비밀번호"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
          </div>

          <div className="flex flex-col gap-3 pt-4">
            <button
              onClick={() => handleLogin("inmemory")}
              disabled={isLoading}
              className={`group relative flex w-full justify-center rounded-md py-2 px-4 text-white font-medium primary ${
                activeEndpoint === "inmemory"
                  ? "bg-[var(--button-primary)]"
                  : "bg-[var(--button-secondary)] hover:opacity-90 focus:outline-none focus:ring-2 focus:ring-[var(--button-primary)] focus:ring-offset-2"
              } ${isLoading ? "opacity-70 cursor-not-allowed" : ""}`}
            >
              {isLoading && activeEndpoint === "inmemory" ? (
                <svg
                  className="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                >
                  <circle
                    className="opacity-25"
                    cx="12"
                    cy="12"
                    r="10"
                    stroke="currentColor"
                    strokeWidth="4"
                  ></circle>
                  <path
                    className="opacity-75"
                    fill="currentColor"
                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                  ></path>
                </svg>
              ) : null}
              인메모리 로그인
            </button>
            <button
              onClick={() => handleLogin("jpa")}
              disabled={isLoading}
              className={`group relative flex w-full justify-center rounded-md py-2 px-4 text-white font-medium secondary ${
                activeEndpoint === "jpa"
                  ? "bg-[var(--button-primary)]"
                  : "bg-[var(--button-secondary)] hover:opacity-90 focus:outline-none focus:ring-2 focus:ring-[var(--button-primary)] focus:ring-offset-2"
              } ${isLoading ? "opacity-70 cursor-not-allowed" : ""}`}
            >
              {isLoading && activeEndpoint === "jpa" ? (
                <svg
                  className="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                >
                  <circle
                    className="opacity-25"
                    cx="12"
                    cy="12"
                    r="10"
                    stroke="currentColor"
                    strokeWidth="4"
                  ></circle>
                  <path
                    className="opacity-75"
                    fill="currentColor"
                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                  ></path>
                </svg>
              ) : null}
              JPA 로그인
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
