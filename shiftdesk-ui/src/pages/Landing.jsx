import { Link } from 'react-router-dom'

export default function Landing() {
  return (
    <main className="page">
      <section className="hero">
        <h1>ShiftDesk</h1>
        <p>Shift scheduling for multi-store teams. Managers publish shifts, employees claim them, and nobody gets double-booked.</p>
        <div><Link className="btn" to="/register">Get started</Link> <Link className="btn ghost" to="/login">Login</Link></div>
      </section>
      <section id="about" className="glass block"><h2>About</h2>
        <p>ShiftDesk replaces paper rosters and group chats. Every store has its own schedule, and every shift is tied to a job role.</p></section>
      <section id="service" className="glass block"><h2>Service</h2>
        <p>Managers create, edit and delete shifts for their store. Employees see open shifts that match their role and claim them in one click. Overlapping claims are blocked automatically.</p></section>
      <section id="contact" className="glass block"><h2>Contact</h2>
        <p>Need access or a role change? Ask your store admin.</p></section>
    </main>
  )
}
