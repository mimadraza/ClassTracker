import { useState } from 'react'
import { fmtMoney, fmtDay } from '../utils.js'

function EntryForm({ classTypes, initial, onSubmit, onCancel }) {
  const [classTypeId, setClassTypeId] = useState(initial?.classTypeId ?? classTypes[0]?.id)
  const [rate, setRate] = useState(initial ? String(initial.rate) : '')
  const [confirmed, setConfirmed] = useState(initial ? initial.confirmed : true)
  const [notes, setNotes] = useState(initial?.notes ?? '')

  const selectedType = classTypes.find((t) => t.id === Number(classTypeId))

  function submit(e) {
    e.preventDefault()
    onSubmit({
      classTypeId: Number(classTypeId),
      rate: rate === '' ? null : Number(rate),
      confirmed,
      notes: notes.trim() || null,
    })
  }

  return (
    <form className="entry-form" onSubmit={submit}>
      <label>
        Class type
        <select value={classTypeId} onChange={(e) => setClassTypeId(e.target.value)}>
          {classTypes.map((t) => (
            <option key={t.id} value={t.id}>
              {t.name} ({fmtMoney(t.defaultRate, t.currency)})
            </option>
          ))}
        </select>
      </label>
      <label>
        Rate {selectedType && <span className="muted">(blank = default {fmtMoney(selectedType.defaultRate, selectedType.currency)})</span>}
        <input
          type="number"
          step="0.01"
          min="0"
          value={rate}
          onChange={(e) => setRate(e.target.value)}
          placeholder={selectedType ? String(selectedType.defaultRate) : ''}
        />
      </label>
      <label>
        Notes
        <input value={notes} onChange={(e) => setNotes(e.target.value)} placeholder="optional" />
      </label>
      <label className="check-row">
        <input type="checkbox" checked={confirmed} onChange={(e) => setConfirmed(e.target.checked)} />
        Confirmed (counts toward total)
      </label>
      <div className="form-actions">
        <button type="button" className="btn" onClick={onCancel}>Cancel</button>
        <button className="btn btn-primary">{initial ? 'Save changes' : 'Add class'}</button>
      </div>
    </form>
  )
}

export default function DayModal({
  date, entries, classTypes, locked,
  onClose, onQuickLog, onAdd, onUpdate, onToggle, onDelete, onManageTypes,
}) {
  const [mode, setMode] = useState('list') // 'list' | 'add' | entry id being edited

  const editingEntry = typeof mode === 'number' ? entries.find((e) => e.id === mode) : null

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <header className="modal-header">
          <h3>{fmtDay(date)}</h3>
          <button className="btn-icon-sm" onClick={onClose} aria-label="Close">✕</button>
        </header>

        {locked && <div className="lock-banner">This month is locked — unlock it to make changes.</div>}

        {mode === 'list' && (
          <>
            {entries.length > 0 && (
              <ul className="entry-rows modal-entries">
                {entries.map((entry) => (
                  <li key={entry.id} className={`entry-row ${entry.confirmed ? 'is-confirmed' : 'is-draft'}`}>
                    <label className="entry-check">
                      <input
                        type="checkbox"
                        checked={entry.confirmed}
                        disabled={locked}
                        onChange={() => onToggle(entry)}
                      />
                    </label>
                    <span className="entry-main">
                      <span className="entry-type">{entry.classTypeName}</span>
                      {entry.notes && <span className="entry-notes">{entry.notes}</span>}
                    </span>
                    <span className="entry-rate">{fmtMoney(entry.rate, entry.currency)}</span>
                    {!locked && (
                      <>
                        <button className="btn-icon-sm" onClick={() => setMode(entry.id)} aria-label="Edit" title="Edit">✎</button>
                        <button className="btn-icon-sm" onClick={() => onDelete(entry)} aria-label="Delete" title="Delete">✕</button>
                      </>
                    )}
                  </li>
                ))}
              </ul>
            )}

            {!locked && classTypes.length > 0 && (
              <div className="quick-log">
                <p className="quick-log-label">Quick log (added as confirmed):</p>
                <div className="quick-log-buttons">
                  {classTypes.map((t) => (
                    <button key={t.id} className="btn btn-chip" onClick={() => onQuickLog(t.id)}>
                      + {t.name} · {fmtMoney(t.defaultRate, t.currency)}
                    </button>
                  ))}
                </div>
                <button className="btn-link" onClick={() => setMode('add')}>
                  More options (rate override, notes, draft)…
                </button>
              </div>
            )}

            {!locked && classTypes.length === 0 && (
              <p className="muted">
                No class types yet.{' '}
                <button className="btn-link" onClick={onManageTypes}>Create your first class type</button>{' '}
                to start logging.
              </p>
            )}
          </>
        )}

        {mode === 'add' && (
          <EntryForm
            classTypes={classTypes}
            onCancel={() => setMode('list')}
            onSubmit={(payload) => {
              onAdd({ ...payload, date })
              setMode('list')
            }}
          />
        )}

        {editingEntry && (
          <EntryForm
            classTypes={classTypes}
            initial={editingEntry}
            onCancel={() => setMode('list')}
            onSubmit={(payload) => {
              onUpdate(editingEntry.id, { ...payload, date })
              setMode('list')
            }}
          />
        )}
      </div>
    </div>
  )
}
