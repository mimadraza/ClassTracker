import { useState } from 'react'
import { fmtMoney } from '../utils.js'

function TypeForm({ initial, onSubmit, onCancel }) {
  const [name, setName] = useState(initial?.name ?? '')
  const [defaultRate, setDefaultRate] = useState(initial ? String(initial.defaultRate) : '')
  const [currency, setCurrency] = useState(initial?.currency ?? 'USD')

  function submit(e) {
    e.preventDefault()
    onSubmit({ name: name.trim(), defaultRate: Number(defaultRate), currency: currency.trim().toUpperCase() })
  }

  return (
    <form className="entry-form" onSubmit={submit}>
      <label>
        Name
        <input value={name} onChange={(e) => setName(e.target.value)} required placeholder="e.g. Math – Grade 9" />
      </label>
      <div className="form-row">
        <label>
          Rate per class
          <input type="number" step="0.01" min="0" value={defaultRate} onChange={(e) => setDefaultRate(e.target.value)} required />
        </label>
        <label>
          Currency
          <input value={currency} onChange={(e) => setCurrency(e.target.value)} maxLength={5} required />
        </label>
      </div>
      <div className="form-actions">
        <button type="button" className="btn" onClick={onCancel}>Cancel</button>
        <button className="btn btn-primary">{initial ? 'Save' : 'Add class type'}</button>
      </div>
    </form>
  )
}

export default function ClassTypeManager({ classTypes, onClose, onCreate, onUpdate, onDelete }) {
  const [mode, setMode] = useState(classTypes.length === 0 ? 'add' : 'list')

  const editing = typeof mode === 'number' ? classTypes.find((t) => t.id === mode) : null

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <header className="modal-header">
          <h3>Class types</h3>
          <button className="btn-icon-sm" onClick={onClose} aria-label="Close">✕</button>
        </header>

        {mode === 'list' && (
          <>
            {classTypes.length === 0 && <p className="muted">No class types yet.</p>}
            <ul className="entry-rows modal-entries">
              {classTypes.map((t) => (
                <li key={t.id} className="entry-row">
                  <span className="entry-main">
                    <span className="entry-type">{t.name}</span>
                  </span>
                  <span className="entry-rate">{fmtMoney(t.defaultRate, t.currency)}</span>
                  <button className="btn-icon-sm" onClick={() => setMode(t.id)} aria-label="Edit" title="Edit">✎</button>
                  <button className="btn-icon-sm" onClick={() => onDelete(t.id)} aria-label="Delete" title="Delete">✕</button>
                </li>
              ))}
            </ul>
            <button className="btn btn-primary" onClick={() => setMode('add')}>+ New class type</button>
          </>
        )}

        {mode === 'add' && (
          <TypeForm
            onCancel={() => setMode('list')}
            onSubmit={(payload) => {
              onCreate(payload)
              setMode('list')
            }}
          />
        )}

        {editing && (
          <TypeForm
            initial={editing}
            onCancel={() => setMode('list')}
            onSubmit={(payload) => {
              onUpdate(editing.id, payload)
              setMode('list')
            }}
          />
        )}
      </div>
    </div>
  )
}
