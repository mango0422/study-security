// src/pages/Signup.tsx
import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuthContext } from '../context/auth'
import { useLoginType } from '../hooks/useLoginType'

export default function Signup() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()
  const { signup } = useAuthContext()
  const loginType = useLoginType()

  const handleSignup = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      await signup(email, password)
      // 가입 후 랜딩 페이지(혹은 홈)으로 이동
      navigate('/home')
    } catch (err) {
      console.error('Signup failed', err)
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-[var(--color-bg-light)] dark:bg-[var(--color-bg-dark)]">
      <form onSubmit={handleSignup} className="form-container">
        <h1 className="mb-4 text-2xl font-bold">Sign Up ({loginType})</h1>
        <input
          className="input mb-3"
          type="email"
          placeholder="Email"
          value={email}
          onChange={e => setEmail(e.target.value)}
        />
        <input
          className="input mb-4"
          type="password"
          placeholder="Password"
          value={password}
          onChange={e => setPassword(e.target.value)}
        />
        <button className="btn" type="submit">
          Sign Up
        </button>
        <div className="mt-4 text-center text-sm">
          Already have an account?{' '}
          <Link to="/" className="text-[var(--color-primary)]">
            Login
          </Link>
        </div>
      </form>
    </div>
  )
}
