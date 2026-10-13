import { useEffect, useState } from 'react'
import { api } from '../api.js'

export default function Admin() {
  const [users, setUsers] = useState([])
  const [msg, setMsg] = useState('')
  const [store, setStore] = useState('')
  const load = () => api('/admin/users').then(setUsers).catch(e => setMsg(e.message))
  useEffect(() => { load() }, [])

  const promote = id => api(`/admin/users/${id}/promote`, { method: 'POST' }).then(() => setMsg('Promoted to manager')).catch(e => setMsg(e.message)).then(load)
  const addStore = e => {
    e.preventDefault()
    api('/admin/stores', { method: 'POST', body: { name: store } }).then(() => { setMsg('Store created'); setStore('') }).catch(x => setMsg(x.message))
  }

  return (
    <main className="page">
      <h1 className="title">Admin <small>users and stores</small></h1>
      {msg && <p className="glass note">{msg}</p>}
      <form onSubmit={addStore} className="glass block row">
        <strong>New store</strong>
        <input placeholder="Store name" required value={store} onChange={e => setStore(e.target.value)} />
        <button className="btn">Create</button>
      </form>
      <div className="glass block scroll">
        <table>
          <thead><tr><th>Name</th><th>Email</th><th>Role</th><th>Store</th><th>Action</th></tr></thead>
          <tbody>
            {users.map(u => (
              <tr key={u.id}>
                <td>{u.name}</td><td>{u.email}</td><td>{u.role}</td><td>{u.storeName || '-'}</td>
                <td>{u.role !== 'ADMIN' && u.role !== 'MANAGER' && <button className="btn sm" onClick={() => promote(u.id)}>Make manager</button>}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </main>
  )
}
