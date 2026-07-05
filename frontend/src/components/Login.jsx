import { useState } from 'react'
import { api } from '../api.js'

export default function Login({ onAuth }) {
  const [mode, setMode] = useState('login')
  const [userName, setUserName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)

  async function submit(e) {
    e.preventDefault()
    setError('')
    setBusy(true)
    try {
      const path = mode === 'login' ? '/api/auth/login' : '/api/auth/signup'
      const body = mode === 'login' ? { email, password } : { userName, email, password }
      const data = await api(path, { method: 'POST', body })
      onAuth(data.token, data.user)
    } catch (err) {
      setError(err.message)
    } finally {
      setBusy(false)
    }
  }

  return (
    <div className="auth-page">
      <form className="auth-card" onSubmit={submit}>
        <h1 className="logo">ClassTracker</h1>
        <p className="auth-sub">Log classes. Skip the math.</p>

        {mode === 'signup' && (
          <label>
            Your name
            <input value={userName} onChange={(e) => setUserName(e.target.value)} required placeholder="e.g. Imad" />
          </label>
        )}
        <label>
          Email
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required placeholder="you@example.com" />
        </label>
        <label>
          Password
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required minLength={6} placeholder="min 6 characters" />
        </label>

        {error && <div className="error-box">{error}</div>}

        <button className="btn btn-primary btn-block" disabled={busy}>
          {busy ? '…' : mode === 'login' ? 'Log in' : 'Create account'}
        </button>

        <button
          type="button"
          className="btn-link"
          onClick={() => {
            setMode(mode === 'login' ? 'signup' : 'login')
            setError('')
          }}
        >
          {mode === 'login' ? "New here? Create an account" : 'Already have an account? Log in'}
        </button>
      </form>
    </div>
  )
}
