import { useEffect, useState } from 'react'
import { api } from '../api.js'
import { useAuth } from '../AuthContext.jsx'

const ROLES = ['CASHIER', 'STOCK_CLERK', 'SUPERVISOR']
const blank = { date: '', start: '09:00', end: '17:00', role: 'CASHIER' }

export default function Dashboard() {
  const { user } = useAuth()
  const isMgr = user.role === 'MANAGER'
  const [shifts, setShifts] = useState([])
  const [msg, setMsg] = useState('')
  const [f, setF] = useState(blank)
  const [editId, setEditId] = useState(null)

  const load = () => api(`/stores/${user.storeId}/shifts`).then(setShifts).catch(e => setMsg(e.message))
  useEffect(() => { load() }, [])
  const run = async (fn, ok) => { try { await fn(); setMsg(ok) } catch (e) { setMsg(e.message) } load() }
  const reset = () => { setEditId(null); setF(blank) }

  const claim = id => run(() => api(`/shifts/${id}/claim`, { method: 'POST' }), 'Shift claimed!')
  const release = id => run(() => api(`/shifts/${id}/release`, { method: 'POST' }), 'Shift released')
  const del = id => window.confirm('Delete this shift?') && run(() => api(`/shifts/${id}`, { method: 'DELETE' }), 'Shift deleted')
  const edit = s => { setEditId(s.id); setF({ date: s.shiftDate, start: s.startTime.slice(0, 5), end: s.endTime.slice(0, 5), role: s.role }) }
  const submit = async e => {
    e.preventDefault()
    await run(() => editId ? api(`/shifts/${editId}`, { method: 'PUT', body: f }) : api('/shifts', { method: 'POST', body: f }), editId ? 'Shift updated' : 'Shift created')
    reset()
  }

  return (
    <main className="page">
      <h1 className="title">{user.storeName} <small>shifts</small></h1>
      {msg && <p className="glass note">{msg}</p>}
      {isMgr && (
        <form onSubmit={submit} className="glass block row">
          <strong>{editId ? 'Edit shift' : 'Publish a shift'}</strong>
          <input type="date" required value={f.date} onChange={e => setF({ ...f, date: e.target.value })} />
          <input type="time" value={f.start} onChange={e => setF({ ...f, start: e.target.value })} />
          <input type="time" value={f.end} onChange={e => setF({ ...f, end: e.target.value })} />
          <select value={f.role} onChange={e => setF({ ...f, role: e.target.value })}>{ROLES.map(r => <option key={r}>{r}</option>)}</select>
          <button className="btn">{editId ? 'Save' : 'Create'}</button>
          {editId && <button type="button" className="btn ghost" onClick={reset}>Cancel</button>}
        </form>
      )}
      <div className="glass block scroll">
        <table>
          <thead><tr><th>Date</th><th>Time</th><th>Role</th><th>Status</th><th>Actions</th></tr></thead>
          <tbody>
            {shifts.length === 0 && <tr><td colSpan="5">No shifts yet.</td></tr>}
            {shifts.map(s => (
              <tr key={s.id}>
                <td>{s.shiftDate}</td><td>{s.startTime.slice(0, 5)} - {s.endTime.slice(0, 5)}</td><td>{s.role}</td>
                <td>{s.claimedBy ? (s.claimedBy.id === user.id ? 'You' : `Taken by ${s.claimedBy.name}`) : 'Open'}</td>
                <td className="actions">
                  {!isMgr && !s.claimedBy && <button className="btn sm" onClick={() => claim(s.id)}>Claim</button>}
                  {isMgr && s.claimedBy && <button className="btn sm" onClick={() => release(s.id)}>Release</button>}
                  {isMgr && !s.claimedBy && <><button className="btn sm" onClick={() => edit(s)}>Edit</button><button className="btn sm danger" onClick={() => del(s.id)}>Delete</button></>}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </main>
  )
}
