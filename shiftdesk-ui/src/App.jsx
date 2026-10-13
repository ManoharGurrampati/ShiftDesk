import { Link, Navigate, Route, Routes, useNavigate } from 'react-router-dom'
import { AuthProvider, useAuth } from './AuthContext.jsx'
import Landing from './pages/Landing.jsx'
import Login from './pages/Login.jsx'
import Register from './pages/Register.jsx'
import Dashboard from './pages/Dashboard.jsx'
import Admin from './pages/Admin.jsx'

function Background() {
  return (
    <svg className="bg" viewBox="0 0 1440 900" preserveAspectRatio="xMidYMax slice">
      <defs>
        <linearGradient id="sky" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stopColor="#d6e6ff" /><stop offset="1" stopColor="#f5f9ff" /></linearGradient>
        <g id="pine" fill="#0b2f55"><polygon points="0,0 -26,70 26,70" /><polygon points="0,40 -34,120 34,120" /><polygon points="0,90 -42,180 42,180" /></g>
      </defs>
      <rect width="1440" height="900" fill="url(#sky)" />
      <circle cx="720" cy="200" r="100" fill="#fff" />
      <path d="M0 520 L220 340 L420 480 L640 300 L860 470 L1100 330 L1440 520 V900 H0Z" fill="#c9d9ff" />
      <path d="M0 620 L260 430 L520 600 L780 410 L1040 590 L1260 450 L1440 580 V900 H0Z" fill="#8fb4ee" />
      <path d="M0 720 L300 540 L600 700 L900 520 L1200 690 L1440 600 V900 H0Z" fill="#4f8ccf" />
      <path d="M0 820 L360 660 L700 790 L1060 650 L1440 800 V900 H0Z" fill="#1f5f99" />
      <use href="#pine" x="60" y="660" /><use href="#pine" x="150" y="720" /><use href="#pine" x="20" y="740" />
      <use href="#pine" x="1380" y="650" /><use href="#pine" x="1290" y="720" /><use href="#pine" x="1415" y="740" />
    </svg>
  )
}

function Navbar() {
  const { user, logout } = useAuth()
  const nav = useNavigate()
  return (
    <header className="nav">
      <Link to="/" className="brand">ShiftDesk{user?.storeName && <span> · {user.storeName}</span>}</Link>
      <nav>
        {!user ? (<>
          <Link to="/">Home</Link><a href="/#about">About</a><a href="/#service">Service</a><a href="/#contact">Contact</a>
          <Link to="/login" className="pill">Login</Link>
        </>) : (<>
          <Link to={user.role === 'ADMIN' ? '/admin' : '/dashboard'}>{user.role === 'ADMIN' ? 'Admin' : 'Dashboard'}</Link>
          <span className="who">{user.role === 'ADMIN' ? 'Admin' : `${user.name} · ${user.role}`}</span>
          <button className="pill" onClick={() => { logout(); nav('/') }}>Logout</button>
        </>)}
      </nav>
    </header>
  )
}

function Protected({ role, deny, children }) {
  const { user } = useAuth()
  if (!user) return <Navigate to="/login" replace />
  if (role && user.role !== role) return <Navigate to="/dashboard" replace />
  if (deny && user.role === deny) return <Navigate to="/admin" replace />
  return children
}

export default function App() {
  return (
    <AuthProvider>
      <Background />
      <Navbar />
      <Routes>
        <Route path="/" element={<Landing />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/dashboard" element={<Protected deny="ADMIN"><Dashboard /></Protected>} />
        <Route path="/admin" element={<Protected role="ADMIN"><Admin /></Protected>} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </AuthProvider>
  )
}
