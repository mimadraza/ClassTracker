import { fmtMoney, fmtDay } from '../utils.js'

export default function WeekList({ summary, locked, onToggle, onDelete, onRowClick }) {
  const weeksWithEntries = summary.weeks.filter((w) => w.entries.length > 0)

  if (weeksWithEntries.length === 0) {
    return (
      <div className="week-list no-print">
        <p className="muted empty-hint">
          No classes logged this month yet. Tap a day on the calendar or hit “+ Log class”.
        </p>
      </div>
    )
  }

  return (
    <div className="week-list no-print">
      {weeksWithEntries.map((week) => (
        <section key={week.weekNumber} className="week-block">
          <header className="week-header">
            <span className="week-title">
              Week {week.weekNumber}
              <span className="week-range"> {fmtDay(week.startDate)} – {fmtDay(week.endDate)}</span>
            </span>
            <span className="week-subtotal">
              {week.confirmedCount}/{week.totalCount} confirmed · <strong>{fmtMoney(week.confirmedAmount, summary.currency)}</strong>
            </span>
          </header>
          <ul className="entry-rows">
            {week.entries.map((entry) => (
              <li key={entry.id} className={`entry-row ${entry.confirmed ? 'is-confirmed' : 'is-draft'}`}>
                <label className="entry-check" onClick={(e) => e.stopPropagation()}>
                  <input
                    type="checkbox"
                    checked={entry.confirmed}
                    disabled={locked}
                    onChange={() => onToggle(entry)}
                    title={entry.confirmed ? 'Confirmed — counts toward total' : 'Draft — not counted'}
                  />
                </label>
                <button className="entry-main" onClick={() => onRowClick(entry)}>
                  <span className="entry-date">{fmtDay(entry.date)}</span>
                  <span className="entry-type">{entry.classTypeName}</span>
                  {entry.notes && <span className="entry-notes">{entry.notes}</span>}
                </button>
                <span className="entry-rate">{fmtMoney(entry.rate, entry.currency)}</span>
                <button
                  className="btn-icon-sm"
                  disabled={locked}
                  onClick={() => onDelete(entry)}
                  aria-label="Delete entry"
                  title="Delete"
                >
                  ✕
                </button>
              </li>
            ))}
          </ul>
        </section>
      ))}
    </div>
  )
}
