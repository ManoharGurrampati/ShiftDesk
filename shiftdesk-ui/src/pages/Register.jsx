import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { api } from '../api.js'
import { useAuth } from '../AuthContext.jsx'

const ROLES = ['CASHIER', 'STOCK_CLERK', 'SUPERVISOR']

export default function Register() {
  const { login } = useAuth()
  const nav = useNavigate()
  const [stores, setStores] = useState([])
  const [f, setF] = useState({ name: '', email: '', password: '', storeId: '', role: 'CASHIER' })
  const [err, setErr] = useState('')
  useEffect(() => { api('/stores').then(s => { setStores(s); if (s[0]) setF(p => ({ ...p, storeId: s[0].id })) }).catch(e => setErr(e.message)) }, [])
  const set = k => e => setF({ ...f, [k]: e.target.value })
  const submit = async e => {
    e.preventDefault()
    try { const d = await api('/auth/register', { method: 'POST', body: { ...f, storeId: Number(f.storeId) } }); login(d); nav('/dashboard') }
    catch (x) { setErr(x.message) }
  }
  return (
    <main className="center">
      <form className="glass card" onSubmit={submit}>
        <h2>REGISTER</h2>
        <input placeholder="Full name" required value={f.name} onChange={set('name')} />
        <input type="email" placeholder="Email" required value={f.email} onChange={set('email')} />
        <input type="password" placeholder="Password (min 6 characters)" required value={f.password} onChange={set('password')} />
        <select value={f.storeId} onChange={set('storeId')}>{stores.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}</select>
        <select value={f.role} onChange={set('role')}>{ROLES.map(r => <option key={r}>{r}</option>)}</select>
        {err && <p className="err">{err}</p>}
        <button className="btn wide">Create account</button>
        <p className="sub">Already registered? <Link to="/login">Login</Link></p>
      </form>
    </main>
  )
}
