import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import { useLoginType } from '../hooks/useLoginType'

export default function Home() {
  const navigate = useNavigate()
  const auth = useAuth()
  const loginType = useLoginType()

  const handleLogout = async () => {
    try {
      await auth.logout()
      navigate('/')
    } catch (err) {
      console.error('Logout failed', err)
    }
  }

  return (
    <div className="flex h-screen items-center justify-center">
      <div className="form-container text-center">
        <h1 className="mb-4 text-2xl font-bold">Welcome Home ({loginType})</h1>
        <p className="mb-6">This is your home page.</p>
        <button onClick={handleLogout} className="btn">
          Logout
        </button>
      </div>
    </div>
  )
}
