import { useNavigate } from 'react-router-dom'
import { logout } from '../api/auth'
import { useAuth } from '../context/AuthContext'

export default function Pocetna() {
  const navigate = useNavigate()
  const { osoba, setOsoba } = useAuth()

  async function handleLogout() {
    await logout()
    setOsoba(null)
    navigate('/login')
  }

  return (
    <div className="auth-page">
      <h1>Početna</h1>
      {osoba ? (
        <p>
          Ulogovani ste kao <strong>{osoba.ime} {osoba.prezime}</strong> ({osoba.tip.toLowerCase()}).
        </p>
      ) : (
        <p>Niste ulogovani.</p>
      )}
      <button type="button" className="auth-submit" onClick={handleLogout}>Odjavi se</button>
    </div>
  )
}
