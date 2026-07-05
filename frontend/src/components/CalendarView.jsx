import { toDateStr } from '../utils.js'

const DAY_HEADERS = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']

export default function CalendarView({ year, month, entriesByDate, onDayClick }) {
  const daysInMonth = new Date(year, month, 0).getDate()
  // getDay(): 0=Sun..6=Sat -> offset with Monday as first column
  const firstDayOffset = (new Date(year, month - 1, 1).getDay() + 6) % 7
  const today = new Date()
  const todayStr = toDateStr(today.getFullYear(), today.getMonth() + 1, today.getDate())

  const cells = []
  for (let i = 0; i < firstDayOffset; i++) {
    cells.push(<div key={`pad-${i}`} className="cal-cell cal-empty" />)
  }
  for (let day = 1; day <= daysInMonth; day++) {
    const dateStr = toDateStr(year, month, day)
    const entries = entriesByDate[dateStr] || []
    const confirmed = entries.filter((e) => e.confirmed)
    const dayAmount = confirmed.reduce((sum, e) => sum + e.rate, 0)
    cells.push(
      <button
        key={dateStr}
        className={`cal-cell ${dateStr === todayStr ? 'cal-today' : ''} ${entries.length ? 'cal-has-entries' : ''}`}
        onClick={() => onDayClick(dateStr)}
      >
        <span className="cal-daynum">{day}</span>
        {entries.length > 0 && (
          <span className="cal-badges">
            <span className="cal-count">
              {entries.slice(0, 4).map((e) => (
                <span key={e.id} className={`dot ${e.confirmed ? 'dot-confirmed' : 'dot-draft'}`} />
              ))}
              {entries.length > 4 && <span className="cal-more">+{entries.length - 4}</span>}
            </span>
            {dayAmount > 0 && <span className="cal-amount">{dayAmount}</span>}
          </span>
        )}
      </button>,
    )
  }

  return (
    <div className="calendar no-print">
      <div className="cal-grid cal-headers">
        {DAY_HEADERS.map((h) => (
          <div key={h} className="cal-header">{h}</div>
        ))}
      </div>
      <div className="cal-grid">{cells}</div>
      <div className="cal-legend">
        <span><span className="dot dot-confirmed" /> confirmed</span>
        <span><span className="dot dot-draft" /> draft</span>
      </div>
    </div>
  )
}
