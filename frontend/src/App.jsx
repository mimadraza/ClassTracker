import { useState } from 'react'
import Login from './components/Login.jsx'
import Dashboard from './components/Dashboard.jsx'
import { getToken, getStoredUser, saveSession, clearSession } from './api.js'

export default function App() {
  const [user, setUser] = useState(() => (getToken() ? getStoredUser() : null))

  function handleAuth(token, authedUser) {
    saveSession(token, authedUser)
    setUser(authedUser)
  }

  function handleLogout() {
    clearSession()
    setUser(null)
  }

  if (!user) return <Login onAuth={handleAuth} />
  return <Dashboard user={user} onLogout={handleLogout} />
}
