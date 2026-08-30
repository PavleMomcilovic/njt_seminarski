import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { register, getKatedre, getZvanja, extractErrorMessage } from '../api/auth'
import './Auth.css'

const STATUSI = ['AKTIVAN', 'APSOLVENT', 'NEAKTIVAN']

export default function Register() {
  const navigate = useNavigate()
  const [tip, setTip] = useState('STUDENT')
  const [email, setEmail] = useState('')
  const [sifra, setSifra] = useState('')
  const [ime, setIme] = useState('')
  const [prezime, setPrezime] = useState('')
  const [brojIndeksa, setBrojIndeksa] = useState('')
  const [status, setStatus] = useState(STATUSI[0])
  const [idKatedre, setIdKatedre] = useState('')
  const [idZvanja, setIdZvanja] = useState('')
  const [katedre, setKatedre] = useState([])
  const [zvanja, setZvanja] = useState([])
  const [greska, setGreska] = useState('')
  const [ucitava, setUcitava] = useState(false)

  useEffect(() => {
    getKatedre().then(setKatedre).catch(() => {})
    getZvanja().then(setZvanja).catch(() => {})
  }, [])

  async function handleSubmit(e) {
    e.preventDefault()
    setGreska('')
    setUcitava(true)
    try {
      const dto = { email, sifra, ime, prezime, tip }
      if (tip === 'STUDENT') {
        dto.brojIndeksa = brojIndeksa
        dto.status = status
      } else {
        dto.idKatedre = idKatedre ? Number(idKatedre) : null
        dto.idZvanja = idZvanja ? Number(idZvanja) : null
      }
      await register(dto)
      navigate('/login')
    } catch (err) {
      setGreska(extractErrorMessage(err))
    } finally {
      setUcitava(false)
    }
  }

  return (
    <div className="auth-page">
      <h1>Registracija</h1>

      <div className="auth-tip-toggle">
        <button type="button" className={tip === 'STUDENT' ? 'active' : ''} onClick={() => setTip('STUDENT')}>
          Student
        </button>
        <button type="button" className={tip === 'PROFESOR' ? 'active' : ''} onClick={() => setTip('PROFESOR')}>
          Profesor
        </button>
      </div>

      {greska && <div className="auth-error">{greska}</div>}

      <form onSubmit={handleSubmit}>
        <div className="auth-field">
          <label htmlFor="email">Email</label>
          <input id="email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
        </div>

        <div className="auth-field">
          <label htmlFor="sifra">Lozinka</label>
          <input id="sifra" type="password" value={sifra} onChange={(e) => setSifra(e.target.value)}
                 minLength={9} required />
        </div>

        <div className="auth-field">
          <label htmlFor="ime">Ime</label>
          <input id="ime" type="text" value={ime} onChange={(e) => setIme(e.target.value)} required />
        </div>

        <div className="auth-field">
          <label htmlFor="prezime">Prezime</label>
          <input id="prezime" type="text" value={prezime} onChange={(e) => setPrezime(e.target.value)} required />
        </div>

        {tip === 'STUDENT' ? (
          <>
            <div className="auth-field">
              <label htmlFor="brojIndeksa">Broj indeksa</label>
              <input id="brojIndeksa" type="text" value={brojIndeksa}
                     onChange={(e) => setBrojIndeksa(e.target.value)} required />
            </div>
            <div className="auth-field">
              <label htmlFor="status">Status</label>
              <select id="status" value={status} onChange={(e) => setStatus(e.target.value)}>
                {STATUSI.map((s) => (
                  <option key={s} value={s}>{s}</option>
                ))}
              </select>
            </div>
          </>
        ) : (
          <>
            <div className="auth-field">
              <label htmlFor="idKatedre">Katedra</label>
              <select id="idKatedre" value={idKatedre} onChange={(e) => setIdKatedre(e.target.value)} required>
                <option value="" disabled>Izaberite katedru</option>
                {katedre.map((k) => (
                  <option key={k.idKatedre} value={k.idKatedre}>{k.naziv}</option>
                ))}
              </select>
            </div>
            <div className="auth-field">
              <label htmlFor="idZvanja">Zvanje</label>
              <select id="idZvanja" value={idZvanja} onChange={(e) => setIdZvanja(e.target.value)} required>
                <option value="" disabled>Izaberite zvanje</option>
                {zvanja.map((z) => (
                  <option key={z.idZvanja} value={z.idZvanja}>{z.naziv}</option>
                ))}
              </select>
            </div>
          </>
        )}

        <button type="submit" className="auth-submit" disabled={ucitava}>
          {ucitava ? 'Registracija u toku...' : 'Registruj se'}
        </button>
      </form>

      <div className="auth-switch">
        Već imate nalog? <Link to="/login">Prijavite se</Link>
      </div>
    </div>
  )
}
