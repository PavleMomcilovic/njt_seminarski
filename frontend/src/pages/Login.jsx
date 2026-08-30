import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { login, extractErrorMessage } from '../api/auth'
import { useAuth } from '../context/AuthContext'
import './Auth.css'

export default function Login() {
  const navigate = useNavigate()
  const { setOsoba } = useAuth()
  const [email, setEmail] = useState('')
  const [sifra, setSifra] = useState('')
  const [greska, setGreska] = useState('')
  const [ucitava, setUcitava] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    setGreska('')
    setUcitava(true)
    try {
      const osoba = await login(email, sifra)
      setOsoba(osoba)
      navigate('/pocetna')
    } catch (err) {
      setGreska(extractErrorMessage(err))
    } finally {
      setUcitava(false)
    }
  }

  return (
    <div className="auth-page">
      <h1>Prijava</h1>

      {greska && <div className="auth-error">{greska}</div>}

      <form onSubmit={handleSubmit}>
        <div className="auth-field">
          <label htmlFor="email">Email</label>
          <input id="email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
        </div>

        <div className="auth-field">
          <label htmlFor="sifra">Lozinka</label>
          <input id="sifra" type="password" value={sifra} onChange={(e) => setSifra(e.target.value)} required />
        </div>

        <button type="submit" className="auth-submit" disabled={ucitava}>
          {ucitava ? 'Prijavljivanje...' : 'Prijavi se'}
        </button>
      </form>

      <div className="auth-switch">
        Nemate nalog? <Link to="/registracija">Registrujte se</Link>
      </div>
    </div>
  )
}
