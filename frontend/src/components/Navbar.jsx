import { NavLink } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import './Navbar.css'

export default function Navbar() {
  const { osoba } = useAuth()

  return (
    <nav className="navbar">
      <div className="navbar-logo">ФОНслеђе</div>

      <div className="navbar-links">
        <NavLink to="/glavna" className={({ isActive }) => (isActive ? 'active' : '')}>
          Главна
        </NavLink>
        <NavLink to="/predmeti" className={({ isActive }) => (isActive ? 'active' : '')}>
          Предмети
        </NavLink>
        <NavLink to="/zajednica" className={({ isActive }) => (isActive ? 'active' : '')}>
          Заједница
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
