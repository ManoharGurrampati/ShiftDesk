import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { api } from '../api.js'
import { useAuth } from '../AuthContext.jsx'

export default function Login() {
  const { login } = useAuth()
  const nav = useNavigate()
  const [f, setF] = useState({ email: '', password: '' })
  const [err, setErr] = useState('')
  const submit = async e => {
    e.preventDefault()
    try { const d = await api('/auth/login', { method: 'POST', body: f }); login(d); nav(d.role === 'ADMIN' ? '/admin' : '/dashboard') }
    catch (x) { setErr(x.message) }
  }
  return (
    <main className="center">
      <form className="glass card" onSubmit={submit}>
        <h2>LOGIN</h2>
        <input type="email" placeholder="Email" required value={f.email} onChange={e => setF({ ...f, email: e.target.value })} />
        <input type="password" placeholder="Password" required value={f.password} onChange={e => setF({ ...f, password: e.target.value })} />
        {err && <p className="err">{err}</p>}
        <button className="btn wide">Login</button>
        <p className="sub">Don't have an Account? <Link to="/register">Register</Link></p>
      </form>
    </main>
  )
}
