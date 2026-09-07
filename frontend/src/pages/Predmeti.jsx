import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { FaCheckCircle, FaTimesCircle } from 'react-icons/fa'
import Overlay from '../components/Overlay'
import { getPredmeti, createPredmet, prijaviSeZaPredmet, odjaviSeZaPredmet } from '../api/predmeti'
import { extractErrorMessage } from '../api/auth'
import { odgovaraPretrazi } from '../utils/pretraga'
import './Auth.css'
import './Predmeti.css'
import { useAuth } from '../context/AuthContext'

const PRAZAN_PREDMET = { godina: '', naziv: '', semestar: '' }

export default function Predmeti() {
  const navigate = useNavigate()
  const { osoba } = useAuth()
  const [predmeti, setPredmeti] = useState([])
  const [formaPredmet, setFormaPredmet] = useState(null)
  const [greska, setGreska] = useState('')
  const [cuva, setCuva] = useState(false)
  const [pretraga, setPretraga] = useState('')

  const jeProfesor = osoba?.tip === 'PROFESOR'

  const filtriraniPredmeti = useMemo(
    () => predmeti.filter((p) => odgovaraPretrazi(p.naziv, pretraga)),
    [predmeti, pretraga]
  )

  useEffect(() => {
    getPredmeti().then(setPredmeti).catch(() => { })
  }, [])

  function otvoriNoviPredmet() {
    setGreska('')
    setFormaPredmet({ ...PRAZAN_PREDMET })
  }

  async function promeniPredavanje(predmet, vecPredaje) {
    setGreska('')
    try {
      const azuriran = vecPredaje
        ? await odjaviSeZaPredmet(predmet.idPredmeta)
        : await prijaviSeZaPredmet(predmet.idPredmeta)
      setPredmeti((prev) => prev.map((p) => (p.idPredmeta === predmet.idPredmeta ? azuriran : p)))
    } catch (err) {
      setGreska(extractErrorMessage(err))
    }
  }

  async function sacuvajPredmet(e) {
    e.preventDefault()
    setCuva(true)
    setGreska('')
    try {
      const dto = {
        godina: formaPredmet.godina,
        naziv: formaPredmet.naziv,
        semestar: formaPredmet.semestar,
      }
      const kreiran = await createPredmet(dto)
      setPredmeti((prev) => [kreiran, ...prev])
      setFormaPredmet(null)
    } catch (err) {
      setGreska(extractErrorMessage(err))
    } finally {
      setCuva(false)
    }
  }

  return (
    <div>
      <div className='predmeti-naslov-red'>
        <h1>Предмети</h1>
        {jeProfesor && (
            <button type="button" className="dodaj-predmet-dugme" onClick={otvoriNoviPredmet}>
              + Додај предмет
            </button>
          )}
      </div>

      <input
        type="text"
        className="pretraga-input"
        placeholder="Претражи предмете..."
        value={pretraga}
        onChange={(e) => setPretraga(e.target.value)}
      />

      {greska && <div className="auth-error">{greska}</div>}

      {filtriraniPredmeti.length === 0 ? (
        <p>{predmeti.length === 0 ? 'Тренутно нема предмета.' : 'Нема предмета који одговарају претрази.'}</p>
      ) : (
        <div className="predmeti-grid">
          {filtriraniPredmeti.map((predmet) => {
            const vecPredaje = predmet.idProfesori?.includes(osoba?.idOsobe)
            return (
              <div
                className="predmet-kartica"
                key={predmet.idPredmeta}
                onClick={() => navigate(`/predmeti/${predmet.idPredmeta}`)}
              >
                <div className="predmet-naziv">{predmet.naziv}</div>
                <div className="predmet-info">
                  {predmet.godina}. година, {predmet.semestar}. семестар
                </div>
                {jeProfesor && (
                  <button
                    type="button"
                    className={`predmet-predaje-toggle na-kartici ${vecPredaje ? 'predaje' : 'ne-predaje'}`}
                    onClick={(e) => {
                      e.stopPropagation()
                      promeniPredavanje(predmet, vecPredaje)
                    }}
                    title={vecPredaje ? 'Предајете овај предмет' : 'Не предајете овај предмет'}
                    aria-label={vecPredaje ? 'Одјавите се са предавања предмета' : 'Пријавите се да предајете предмет'}
                  >
                    {vecPredaje ? <FaCheckCircle size={18} /> : <FaTimesCircle size={18} />}
                  </button>
                )}
              </div>
            )
          })}
        </div>
      )}

      {formaPredmet && (
        <Overlay onClose={() => setFormaPredmet(null)}>
          <h2>{'Нови предмет'}</h2>
          {greska && <div className="auth-error">{greska}</div>}
          <form onSubmit={sacuvajPredmet}>
            <div className="auth-field">
              <label htmlFor="predmet-godina">Година</label>
              <input
                id="predmet-godina"
                type="number"
                min={1}
                max={4}
                step={1}
                value={formaPredmet.godina}
                onChange={(e) => setFormaPredmet({ ...formaPredmet, godina: e.target.value })}
                required
              />
            </div>
            <div className="auth-field">
              <label htmlFor="predmet-naziv">Назив</label>
              <input
                id="predmet-naziv"
                type="text"
                value={formaPredmet.naziv}
                onChange={(e) => setFormaPredmet({ ...formaPredmet, naziv: e.target.value })}
                required
              />
            </div>
            <div className="auth-field">
              <label htmlFor="predmet-semestar">Семестар</label>
              <input
                id="predmet-semestar"
                type="number"
                min={1}
                max={2}
                step={1}
                value={formaPredmet.semestar}
                onChange={(e) => setFormaPredmet({ ...formaPredmet, semestar: e.target.value })}
                required
              />
            </div>
            <button type="submit" className="auth-submit" disabled={cuva}>
              {cuva ? 'Чување...' : 'Сачувај'}
            </button>
          </form>
        </Overlay>
      )}
    </div>
  )
}
