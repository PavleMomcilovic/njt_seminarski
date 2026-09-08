import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { login, extractErrorMessage } from '../api/auth'
import { useAuth } from '../context/AuthContext'
import './Auth.css'
import { FaEye, FaEyeSlash } from 'react-icons/fa'

export default function Login() {
  const navigate = useNavigate()
  const { setOsoba } = useAuth()
  const [email, setEmail] = useState('')
  const [sifra, setSifra] = useState('')
  const [prikazSifre, setPrikazSifre] = useState(false)
  const [greska, setGreska] = useState('')
  const [ucitava, setUcitava] = useState(false)

  const promeniVidljivostSifre = () => {
    setPrikazSifre((prev) => !prev)
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setGreska('')
    setUcitava(true)
    try {
      const osoba = await login(email, sifra)
      setOsoba(osoba)
      navigate('/glavna')
    } catch (err) {
      setGreska(extractErrorMessage(err))
    } finally {
      setUcitava(false)
    }
  }

  return (
    <div className="auth-page">
      <h1>Пријава</h1>

      {greska && <div className="auth-error">{greska}</div>}

      <form onSubmit={handleSubmit}>
        <div className="auth-field">
          <label htmlFor="email">Имејл</label>
          <input id="email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
        </div>

        <div className="auth-field">
          <label htmlFor="sifra">Лозинка</label>
          <div className="input-wrapper">
            <input
              id="sifra"
              type={prikazSifre ? "text" : "password"}
              value={sifra}
              onChange={(e) => setSifra(e.target.value)}
              required
            />

            <button
             type="button"
             onClick={promeniVidljivostSifre}
             className="icon-button"
             aria-label={prikazSifre ? "Сакриј шифру" : "Прикажи шифру"}
            >
              {prikazSifre ? <FaEyeSlash size={20}/> : <FaEye size={20}/>}
            </button>
          </div>
        </div>

        <button type="submit" className="auth-submit" disabled={ucitava}>
          {ucitava ? 'Пријављивање...' : 'Пријавите се'}
        </button>
      </form>

      <div className="auth-switch">
        Немате налог? <Link to="/registracija">Региструјте се</Link>
      </div>
    </div>
  )
}
