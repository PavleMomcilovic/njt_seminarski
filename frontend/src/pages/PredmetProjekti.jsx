import { useEffect, useMemo, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { FaCheckCircle, FaTimesCircle } from 'react-icons/fa'
import Overlay from '../components/Overlay'
import Potvrda from '../components/Potvrda'
import { getPredmetById, updatePredmet, deletePredmet, prijaviSeZaPredmet, odjaviSeZaPredmet } from '../api/predmeti'
import { getProjektiByPredmet, deleteProjekat } from '../api/projekti'
import { getOsobe } from '../api/osobe'
import { getVerifikacijeZaStudenta } from '../api/verifikacije'
import { extractErrorMessage } from '../api/auth'
import { odgovaraPretrazi } from '../utils/pretraga'
import { useAuth } from '../context/AuthContext'
import './Auth.css'
import './Predmeti.css'

export default function PredmetProjekti() {
  const { id } = useParams()
  const navigate = useNavigate()
  const { osoba } = useAuth()
  const [predmet, setPredmet] = useState(null)
  const [projekti, setProjekti] = useState([])
  const [osobe, setOsobe] = useState([])
  const [verifikovan, setVerifikovan] = useState(false)
  const [formaPredmet, setFormaPredmet] = useState(null)
  const [greska, setGreska] = useState('')
  const [cuva, setCuva] = useState(false)
  const [pretraga, setPretraga] = useState('')
  const [potvrda, setPotvrda] = useState(null)

  const jeProfesor = osoba?.tip === 'PROFESOR'
  const vecPredaje = predmet?.idProfesori?.includes(osoba?.idOsobe)

  const filtriraniProjekti = useMemo(
    () => projekti.filter((p) => odgovaraPretrazi([p.naziv, p.opis], pretraga)),
    [projekti, pretraga]
  )

  useEffect(() => {
    getPredmetById(id).then(setPredmet).catch(() => {})
    getProjektiByPredmet(id).then(setProjekti).catch(() => {})
    getOsobe().then(setOsobe).catch(() => {})

    if (osoba?.tip === 'STUDENT') {
      getVerifikacijeZaStudenta(osoba.idOsobe)
        .then((verifikacije) =>
          setVerifikovan(verifikacije.some((v) => v.idPredmeta === Number(id) && v.status))
        )
        .catch(() => {})
    }
  }, [id, osoba])

  function nazivAutora(idStudenta) {
    const autor = osobe.find((o) => o.idOsobe === idStudenta)
    return autor ? `${autor.ime} ${autor.prezime}` : `студент #${idStudenta}`
  }

  function trazipotvrduBrisanjaProjekta(projekat) {
    setPotvrda({ poruka: 'Обрисати овај пројекат?', akcija: () => obrisiProjekat(projekat) })
  }

  async function obrisiProjekat(projekat) {
    setGreska('')
    try {
      await deleteProjekat(projekat.idProjekta)
      setProjekti((prev) => prev.filter((p) => p.idProjekta !== projekat.idProjekta))
    } catch (err) {
      setGreska(extractErrorMessage(err))
    }
  }

  function otvoriIzmenuPredmeta() {
    setGreska('')
    setFormaPredmet({
      idPredmeta: predmet.idPredmeta,
      naziv: predmet.naziv,
      godina: predmet.godina,
      semestar: predmet.semestar,
    })
  }

  async function promeniPredavanje() {
    setGreska('')
    try {
      const azuriran = vecPredaje
        ? await odjaviSeZaPredmet(predmet.idPredmeta)
        : await prijaviSeZaPredmet(predmet.idPredmeta)
      setPredmet(azuriran)
    } catch (err) {
      setGreska(extractErrorMessage(err))
    }
  }

  function trazipotvrduBrisanjaPredmeta() {
    setPotvrda({ poruka: 'Обрисати овај предмет?', akcija: obrisiPredmet })
  }

  async function obrisiPredmet() {
    setGreska('')
    try {
      await deletePredmet(predmet.idPredmeta)
      navigate('/predmeti')
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
        naziv: formaPredmet.naziv,
        godina: Number(formaPredmet.godina),
        semestar: Number(formaPredmet.semestar),
      }
      const azuriran = await updatePredmet(formaPredmet.idPredmeta, dto)
      setPredmet(azuriran)
      setFormaPredmet(null)
    } catch (err) {
      setGreska(extractErrorMessage(err))
    } finally {
      setCuva(false)
    }
  }

  return (
    <div>
      <div className="predmeti-naslov-red">
        <h1>{predmet ? predmet.naziv : 'Предмет'}</h1>
        {jeProfesor && predmet && (
          <div className="predmet-akcije">
            <button
              type="button"
              className={`predmet-predaje-toggle ${vecPredaje ? 'predaje' : 'ne-predaje'}`}
              onClick={promeniPredavanje}
              title={vecPredaje ? 'Предајете овај предмет' : 'Не предајете овај предмет'}
              aria-label={vecPredaje ? 'Одјавите се са предавања предмета' : 'Пријавите се да предајете предмет'}
            >
              {vecPredaje ? <FaCheckCircle size={18} /> : <FaTimesCircle size={18} />}
            </button>
            <button type="button" className="predmet-izmeni" onClick={otvoriIzmenuPredmeta}>
              Измени
            </button>
            <button type="button" className="predmet-obrisi" onClick={trazipotvrduBrisanjaPredmeta}>
              Обриши
            </button>
          </div>
        )}
      </div>

      {greska && <div className="auth-error">{greska}</div>}

      {verifikovan && (
        <Link to={`/predmeti/${id}/novi-projekat`} className="dodaj-projekat-dugme">
          + Додај пројекат
        </Link>
      )}

      {projekti.length > 0 && (
        <input
          type="text"
          className="pretraga-input"
          placeholder="Претражи пројекте..."
          value={pretraga}
          onChange={(e) => setPretraga(e.target.value)}
        />
      )}

      {filtriraniProjekti.length === 0 ? (
        <p>
          {projekti.length === 0
            ? 'За овај предмет још нема постављених пројеката.'
            : 'Нема пројеката који одговарају претрази.'}
        </p>
      ) : (
        <div className="predmeti-grid">
          {filtriraniProjekti.map((projekat) => {
            const mozeDaObrise = osoba?.idOsobe === projekat.idStudenta || (jeProfesor && vecPredaje)
            return (
              <div
                className="predmet-kartica"
                key={projekat.idProjekta}
                onClick={() => navigate(`/projekti/${projekat.idProjekta}`)}
              >
                <div className="predmet-naziv">{projekat.naziv}</div>
                <div className="predmet-info">{projekat.opis}</div>
                <Link
                  to={`/osoba/${projekat.idStudenta}`}
                  className="predmet-info"
                  onClick={(e) => e.stopPropagation()}
                >
                  Аутор: {nazivAutora(projekat.idStudenta)}
                </Link>
                {mozeDaObrise && (
                  <button
                    type="button"
                    className="projekat-obrisi"
                    onClick={(e) => {
                      e.stopPropagation()
                      trazipotvrduBrisanjaProjekta(projekat)
                    }}
                  >
                    Обриши
                  </button>
                )}
              </div>
            )
          })}
        </div>
      )}

      {formaPredmet && (
        <Overlay onClose={() => setFormaPredmet(null)}>
          <h2>Измена предмета</h2>
          <form onSubmit={sacuvajPredmet}>
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

      {potvrda && (
        <Potvrda
          poruka={potvrda.poruka}
          onPotvrdi={() => {
            potvrda.akcija()
            setPotvrda(null)
          }}
          onOdustani={() => setPotvrda(null)}
        />
      )}
    </div>
  )
}
