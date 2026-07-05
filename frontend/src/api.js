const TOKEN_KEY = 'classtracker_token'
const USER_KEY = 'classtracker_user'

export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

export function getStoredUser() {
  try {
    return JSON.parse(localStorage.getItem(USER_KEY))
  } catch {
    return null
  }
}

export function saveSession(token, user) {
  localStorage.setItem(TOKEN_KEY, token)
  localStorage.setItem(USER_KEY, JSON.stringify(user))
}

export function clearSession() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
}

export async function api(path, { method = 'GET', body } = {}) {
  const headers = { 'Content-Type': 'application/json' }
  const token = getToken()
  if (token) headers.Authorization = `Bearer ${token}`

  let res
  try {
    res = await fetch(path, {
      method,
      headers,
      body: body !== undefined ? JSON.stringify(body) : undefined,
    })
  } catch {
    throw new Error('cannot reach the server — is the backend running on port 8080?')
  }

  if (res.status === 401) {
    clearSession()
    window.location.reload()
    throw new Error('session expired, please log in again')
  }
  if (res.status === 204) return null

  const isJson = (res.headers.get('content-type') || '').includes('application/json')
  const data = isJson ? await res.json() : await res.text()
  if (!res.ok) {
    if (res.status === 502 || res.status === 504 || (res.status === 500 && !isJson)) {
      throw new Error('backend is not responding — is it running on port 8080?')
    }
    throw new Error((isJson && data && data.error) || `request failed (HTTP ${res.status})`)
  }
  return data
}

export async function downloadCsv(year, month) {
  const res = await fetch(`/api/summary/export.csv?year=${year}&month=${month}`, {
    headers: { Authorization: `Bearer ${getToken()}` },
  })
  if (!res.ok) throw new Error('export failed')
  const blob = await res.blob()
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `classtracker-${year}-${String(month).padStart(2, '0')}.csv`
  document.body.appendChild(a)
  a.click()
  a.remove()
  URL.revokeObjectURL(url)
}
