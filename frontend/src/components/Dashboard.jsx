import { useCallback, useEffect, useMemo, useState } from 'react'
import { api, downloadCsv } from '../api.js'
import { MONTHS, fmtMoney, toDateStr } from '../utils.js'
import CalendarView from './CalendarView.jsx'
import WeekList from './WeekList.jsx'
import DayModal from './DayModal.jsx'
import ClassTypeManager from './ClassTypeManager.jsx'
import PrintSummary from './PrintSummary.jsx'

export default function Dashboard({ user, onLogout }) {
  const today = new Date()
  const [year, setYear] = useState(today.getFullYear())
  const [month, setMonth] = useState(today.getMonth() + 1) // 1-12
  const [summary, setSummary] = useState(null)
  const [classTypes, setClassTypes] = useState([])
  const [selectedDate, setSelectedDate] = useState(null) // 'YYYY-MM-DD'
  const [showTypes, setShowTypes] = useState(false)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  const load = useCallback(async () => {
    try {
      const [sum, types] = await Promise.all([
        api(`/api/summary?year=${year}&month=${month}`),
        api('/api/class-types'),
      ])
      setSummary(sum)
      setClassTypes(types)
      setError('')
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }, [year, month])

  useEffect(() => {
    setLoading(true)
    load()
  }, [load])

  async function mutate(fn) {
    try {
      await fn()
      await load()
      setError('')
    } catch (err) {
      setError(err.message)
    }
  }

  const entriesByDate = useMemo(() => {
    const map = {}
    if (summary) {
      for (const week of summary.weeks) {
        for (const entry of week.entries) {
          ;(map[entry.date] = map[entry.date] || []).push(entry)
        }
      }
    }
    return map
  }, [summary])

  function shiftMonth(delta) {
    let m = month + delta
    let y = year
    if (m < 1) { m = 12; y-- }
    if (m > 12) { m = 1; y++ }
    setMonth(m)
    setYear(y)
    setSelectedDate(null)
  }

  function goToday() {
    const now = new Date()
    setYear(now.getFullYear())
    setMonth(now.getMonth() + 1)
  }

  const locked = summary?.locked
  const isCurrentMonth = year === today.getFullYear() && month === today.getMonth() + 1

  return (
    <div className="app">
      <header className="topbar no-print">
        <span className="logo">ClassTracker</span>
        <span className="topbar-user">
          {user.userName}
          <button className="btn-link" onClick={onLogout}>Log out</button>
        </span>
      </header>

      <main className="container">
        <div className="month-nav no-print">
          <button className="btn btn-icon" onClick={() => shiftMonth(-1)} aria-label="Previous month">‹</button>
          <h2 className="month-title">{MONTHS[month - 1]} {year}</h2>
          <button className="btn btn-icon" onClick={() => shiftMonth(1)} aria-label="Next month">›</button>
          {!isCurrentMonth && <button className="btn-link" onClick={goToday}>Today</button>}
        </div>

        {error && <div className="error-box no-print">{error}</div>}

        {summary && (
          <>
            <div className="stats no-print">
              <div className="stat-card stat-total">
                <span className="stat-label">Confirmed total {locked && <span className="lock-pill">🔒 locked</span>}</span>
                <span className="stat-value">{fmtMoney(summary.totalConfirmedAmount, summary.currency)}</span>
                <span className="stat-sub">
                  {summary.confirmedClasses} of {summary.totalClasses} classes confirmed
                </span>
              </div>
            </div>

            <div className="actions no-print">
              <button
                className="btn btn-primary"
                disabled={locked}
                onClick={() => setSelectedDate(isCurrentMonth ? toDateStr(year, month, today.getDate()) : toDateStr(year, month, 1))}
              >
                + Log class
              </button>
              <button className="btn" onClick={() => setShowTypes(true)}>Class types</button>
              <button className="btn" onClick={() => downloadCsv(year, month).catch((e) => setError(e.message))}>
                Export CSV
              </button>
              <button className="btn" onClick={() => window.print()}>Print / PDF</button>
              <button
                className="btn"
                onClick={() =>
                  mutate(() => api(`/api/summary/${locked ? 'unlock' : 'lock'}?year=${year}&month=${month}`, { method: 'POST' }))
                }
              >
                {locked ? 'Unlock month' : 'Lock month'}
              </button>
            </div>

            <CalendarView
              year={year}
              month={month}
              entriesByDate={entriesByDate}
              onDayClick={(dateStr) => setSelectedDate(dateStr)}
            />

            <WeekList
              summary={summary}
              locked={locked}
              onToggle={(entry) =>
                mutate(() => api(`/api/classes/${entry.id}/confirm`, { method: 'PATCH', body: { confirmed: !entry.confirmed } }))
              }
              onDelete={(entry) => mutate(() => api(`/api/classes/${entry.id}`, { method: 'DELETE' }))}
              onRowClick={(entry) => setSelectedDate(entry.date)}
            />

            <PrintSummary summary={summary} user={user} />
          </>
        )}

        {loading && !summary && <p className="muted">Loading…</p>}
      </main>

      {selectedDate && summary && (
        <DayModal
          date={selectedDate}
          entries={entriesByDate[selectedDate] || []}
          classTypes={classTypes}
          locked={locked}
          onClose={() => setSelectedDate(null)}
          onQuickLog={(typeId) =>
            mutate(() => api('/api/classes', { method: 'POST', body: { classTypeId: typeId, date: selectedDate, confirmed: true } }))
          }
          onAdd={(payload) => mutate(() => api('/api/classes', { method: 'POST', body: payload }))}
          onUpdate={(id, payload) => mutate(() => api(`/api/classes/${id}`, { method: 'PUT', body: payload }))}
          onToggle={(entry) =>
            mutate(() => api(`/api/classes/${entry.id}/confirm`, { method: 'PATCH', body: { confirmed: !entry.confirmed } }))
          }
          onDelete={(entry) => mutate(() => api(`/api/classes/${entry.id}`, { method: 'DELETE' }))}
          onManageTypes={() => { setSelectedDate(null); setShowTypes(true) }}
        />
      )}

      {showTypes && (
        <ClassTypeManager
          classTypes={classTypes}
          onClose={() => setShowTypes(false)}
          onCreate={(payload) => mutate(() => api('/api/class-types', { method: 'POST', body: payload }))}
          onUpdate={(id, payload) => mutate(() => api(`/api/class-types/${id}`, { method: 'PUT', body: payload }))}
          onDelete={(id) => mutate(() => api(`/api/class-types/${id}`, { method: 'DELETE' }))}
        />
      )}
    </div>
  )
}
