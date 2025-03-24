// src/components/Nav.tsx
import { Link, useLocation, useNavigate } from 'react-router-dom'
import { useEffect, useState } from 'react'
import { useLoginType } from '../hooks/useLoginType'
import securityIcon from '../assets/spring-security.svg'

export default function Nav() {
  const location = useLocation()
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const navigate = useNavigate()
  const loginType = useLoginType()

  const navigateToHome = () => {
    navigate('/home')
  }

  useEffect(() => {
    setIsLoggedIn(location.pathname === '/home')
  }, [location])

  return (
    <nav className="fixed top-0 right-0 left-0 z-10 bg-stone-700 shadow-md">
      <div className="container mx-auto flex items-center justify-between p-4">
        <div className="flex items-center space-x-3" onClick={navigateToHome}>
          <div className="h-12 w-12">
            <img src={securityIcon} alt="Spring Security Logo" className="neon-svg" />
          </div>
          <div className="text-4xl font-bold">
            <span className="neon-spring">Spring</span>
            <span className="neon-security"> Security</span>
          </div>
        </div>
        <div className="flex space-x-4">
          {isLoggedIn ? (
            <>
              <Link
                to="/home"
                className={`transition-colors hover:text-[#00FF80] ${location.pathname === '/home' ? 'text-[#00FF80]' : ''}`}
              >
                Home
              </Link>
              <Link to="/logout" className="transition-colors hover:text-[#00FF80]">
                Logout
              </Link>
            </>
          ) : (
            <>
              {/* 로그인 페이지 링크는 현재 로그인 타입에 따라 다르게 지정 */}
              <Link
                to={
                  loginType === 'JPA'
                    ? '/jpa/login'
                    : loginType === 'OAuth'
                      ? '/oauth/login'
                      : '/inmemory/login'
                }
                className={`transition-colors hover:text-[#00FF80] ${location.pathname.endsWith('login') ? 'text-[#00FF80]' : ''}`}
              >
                Login
              </Link>
              <Link
                to="/signup"
                className={`transition-colors hover:text-[#00FF80] ${location.pathname === '/signup' ? 'text-[#00FF80]' : ''}`}
              >
                Signup
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  )
}
