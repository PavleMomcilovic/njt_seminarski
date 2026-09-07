import { NavLink } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import './Navbar.css'

export default function Navbar() {
  const { osoba } = useAuth()

  return (
    <nav className="navbar">
      <div className="navbar-logo">FONsleđe</div>

      <div className="navbar-links">
        <NavLink to="/glavna" className={({ isActive }) => (isActive ? 'active' : '')}>
          Glavna
        </NavLink>
        <NavLink to="/predmeti" className={({ isActive }) => (isActive ? 'active' : '')}>
          Predmeti
        </NavLink>
        <NavLink to="/zajednica" className={({ isActive }) => (isActive ? 'active' : '')}>
          Zajednica
        </NavLink>
      </div>

      {osoba && (
        <NavLink to={`/osoba/${osoba.idOsobe}`} className="navbar-account">
          {osoba.ime} {osoba.prezime}
        </NavLink>
      )}
    </nav>
  )
}
