import { MONTHS, fmtMoney, fmtDay } from '../utils.js'

/** Hidden on screen; becomes the whole page when printing (see index.css @media print). */
export default function PrintSummary({ summary, user }) {
  return (
    <div className="print-summary">
      <h1>Monthly Class Summary — {MONTHS[summary.month - 1]} {summary.year}</h1>
      <p>
        Tutor: <strong>{user.userName}</strong>
        {summary.locked ? ' · Month locked (final)' : ' · Draft (month not locked)'}
      </p>

      {summary.weeks.filter((w) => w.entries.length > 0).map((week) => (
        <div key={week.weekNumber}>
          <h3>Week {week.weekNumber} ({fmtDay(week.startDate)} – {fmtDay(week.endDate)})</h3>
          <table>
            <thead>
              <tr>
                <th>Date</th>
                <th>Class</th>
                <th>Status</th>
                <th>Notes</th>
                <th className="num">Rate</th>
              </tr>
            </thead>
            <tbody>
              {week.entries.map((e) => (
                <tr key={e.id}>
                  <td>{e.date}</td>
                  <td>{e.classTypeName}</td>
                  <td>{e.confirmed ? 'Confirmed' : 'Draft'}</td>
                  <td>{e.notes || ''}</td>
                  <td className="num">{fmtMoney(e.rate, e.currency)}</td>
                </tr>
              ))}
              <tr className="subtotal">
                <td colSpan={4}>Week {week.weekNumber} subtotal ({week.confirmedCount} confirmed)</td>
                <td className="num">{fmtMoney(week.confirmedAmount, summary.currency)}</td>
              </tr>
            </tbody>
          </table>
        </div>
      ))}

      <table className="grand-total">
        <tbody>
          <tr>
            <td>
              <strong>GRAND TOTAL</strong> — {summary.confirmedClasses} confirmed classes
              {summary.totalClasses !== summary.confirmedClasses &&
                ` (${summary.totalClasses - summary.confirmedClasses} draft excluded)`}
            </td>
            <td className="num"><strong>{fmtMoney(summary.totalConfirmedAmount, summary.currency)}</strong></td>
          </tr>
        </tbody>
      </table>
    </div>
  )
}
