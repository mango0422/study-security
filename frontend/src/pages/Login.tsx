// src/pages/Login.tsx
import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import { useLoginType } from '../hooks/useLoginType'

export default function Login() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()
  const auth = useAuth()
  const loginType = useLoginType()

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      await auth.login(username, password)
      navigate('/home')
    } catch (err) {
      console.error('Login failed', err)
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center">
      <form onSubmit={handleLogin} className="form-container">
        <h1 className="mb-2 text-xl font-semibold">Login ({loginType})</h1>
        <input
          type="text"
          placeholder="Username"
          className="input mb-2"
          value={username}
          onChange={e => setUsername(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          className="input mb-4"
          value={password}
          onChange={e => setPassword(e.target.value)}
        />
        <button type="submit" className="btn">
          Login
        </button>
        <div className="mt-4 text-center text-sm">
          Don't have an account?{' '}
          <Link to="/signup" className="text-[var(--color-primary)]">
            Sign up
          </Link>
        </div>
      </form>
    </div>
  )
}
