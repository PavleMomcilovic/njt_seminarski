import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { FaCheckCircle, FaTimesCircle } from 'react-icons/fa'
import Carousel from '../components/Carousel'
import Overlay from '../components/Overlay'
import Potvrda from '../components/Potvrda'
import { getVesti, createVest, updateVest, deleteVest } from '../api/vesti'
import { getPredmeti, createPredmet, prijaviSeZaPredmet, odjaviSeZaPredmet } from '../api/predmeti'
import { extractErrorMessage } from '../api/auth'
import { useAuth } from '../context/AuthContext'
import './Auth.css'
import './Predmeti.css'
import './Glavna.css'

const TEKST_LIMIT = 220
const PRAZNA_VEST = { naziv: '', tekst: '', datum: '' }
const PRAZAN_PREDMET = { godina: '', naziv: '', semestar: '' }

function formatDatum(datum) {
  if (!datum) return ''
  return new Date(datum).toLocaleDateString('sr-RS')
}

export default function Glavna() {
  const navigate = useNavigate()
  const { osoba } = useAuth()
  const [vesti, setVesti] = useState([])
  const [predmeti, setPredmeti] = useState([])
  const [otvorenaVest, setOtvorenaVest] = useState(null)
  const [formaVest, setFormaVest] = useState(null)
  const [formaPredmet, setFormaPredmet] = useState(null)
  const [greska, setGreska] = useState('')
  const [cuva, setCuva] = useState(false)
  const [potvrda, setPotvrda] = useState(null)

  const jeProfesor = osoba?.tip === 'PROFESOR'

  useEffect(() => {
    getVesti().then(setVesti).catch(() => { })
    getPredmeti().then(setPredmeti).catch(() => { })
  }, [])

  function otvoriNovuVest() {
    setGreska('')
    setFormaVest({ ...PRAZNA_VEST })
  }

  function otvoriIzmenuVesti(vest) {
    setGreska('')
    setFormaVest({
      idProfesora: vest.idProfesora,
      idVesti: vest.idVesti,
      naziv: vest.naziv,
      tekst: vest.tekst,
      datum: vest.datum ? vest.datum.slice(0, 10) : '',
    })
  }

  function trazipotvrduBrisanjaVesti(vest) {
    setPotvrda({ poruka: 'Обрисати ову вест?', akcija: () => obrisiVest(vest) })
  }

  async function obrisiVest(vest) {
    setGreska('')
    try {
      await deleteVest(vest.idProfesora, vest.idVesti)
      setVesti((prev) => prev.filter((v) => !(v.idProfesora === vest.idProfesora && v.idVesti === vest.idVesti)))
    } catch (err) {
      setGreska(extractErrorMessage(err))
    }
  }

  async function sacuvajVest(e) {
    e.preventDefault()
    setCuva(true)
    setGreska('')
    try {
      const dto = {
        naziv: formaVest.naziv,
        tekst: formaVest.tekst,
        datum: formaVest.datum || null,
      }
      if (formaVest.idVesti) {
        const azurirana = await updateVest(formaVest.idProfesora, formaVest.idVesti, dto)
        setVesti((prev) =>
          prev.map((v) => (v.idProfesora === azurirana.idProfesora && v.idVesti === azurirana.idVesti ? azurirana : v))
        )
      } else {
        const kreirana = await createVest(dto)
        setVesti((prev) => [kreirana, ...prev])
      }
      setFormaVest(null)
    } catch (err) {
      setGreska(extractErrorMessage(err))
    } finally {
      setCuva(false)
    }
  }

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
      <section className="glavna-sekcija">
        <div className="glavna-naslov-red">
          <h2>Вести</h2>
          {jeProfesor && (
            <button type="button" className="dodaj-objekat-dugme" onClick={otvoriNovuVest}>
              + Додај вест
            </button>
          )}
        </div>
        {vesti.length === 0 ? (
          <p className="glavna-prazno">Тренутно нема вести.</p>
        ) : (
          <Carousel>
            {vesti.map((vest) => {
              const predugacka = vest.tekst.length > TEKST_LIMIT
              return (
                <div className="kartica vest-kartica" key={`${vest.idProfesora}-${vest.idVesti}`}>
                  <div className="kartica-naslov">{vest.naziv}</div>
                  <div className="kartica-datum">{formatDatum(vest.datum)}</div>
                  <p className="kartica-tekst">
                    {predugacka ? vest.tekst.slice(0, TEKST_LIMIT) + '…' : vest.tekst}
                  </p>
                  {predugacka && (
                    <button type="button" className="procitaj-vise" onClick={() => setOtvorenaVest(vest)}>
                      Прочитај више...
                    </button>
                  )}
                  {jeProfesor && (
                    <div className="vest-akcije">
                      <button type="button" className="vest-izmeni" onClick={() => otvoriIzmenuVesti(vest)}>
                        Измени
                      </button>
                      <button type="button" className="vest-obrisi" onClick={() => trazipotvrduBrisanjaVesti(vest)}>
                        Обриши
                      </button>
                    </div>
                  )}
                </div>
              )
            })}
          </Carousel>
        )}
      </section>

      <section className="glavna-sekcija">
        <div className="glavna-naslov-red">
          <h2>Предмети</h2>
          {jeProfesor && (
            <button type="button" className="dodaj-objekat-dugme" onClick={otvoriNoviPredmet}>
              + Додај предмет
            </button>
          )}
        </div>
        {predmeti.length === 0 ? (
          <p className="glavna-prazno">Тренутно нема предмета.</p>
        ) : (
          <Carousel>
            {predmeti.map((predmet) => {
              const vecPredaje = predmet.idProfesori?.includes(osoba?.idOsobe)
              return (
                <div
                  className="kartica predmet-kartica"
                  key={predmet.idPredmeta}
                  onClick={() => navigate(`/predmeti/${predmet.idPredmeta}`)}
                >
                  <div className="kartica-naslov">{predmet.naziv}</div>
                  <div className="kartica-datum">
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
          </Carousel>
        )}
      </section>

      {otvorenaVest && (
        <Overlay onClose={() => setOtvorenaVest(null)}>
          <h2>{otvorenaVest.naziv}</h2>
          <div className="kartica-datum">{formatDatum(otvorenaVest.datum)}</div>
          <p>{otvorenaVest.tekst}</p>
        </Overlay>
      )}

      {formaVest && (
        <Overlay onClose={() => setFormaVest(null)}>
          <h2>{formaVest.idVesti ? 'Измена вести' : 'Нова вест'}</h2>
          {greska && <div className="auth-error">{greska}</div>}
          <form onSubmit={sacuvajVest}>
            <div className="auth-field">
              <label htmlFor="vest-naziv">Наслов</label>
              <input
                id="vest-naziv"
                type="text"
                value={formaVest.naziv}
                onChange={(e) => setFormaVest({ ...formaVest, naziv: e.target.value })}
                required
              />
            </div>
            <div className="auth-field">
              <label htmlFor="vest-tekst">Текст</label>
              <textarea
                id="vest-tekst"
                rows={6}
                value={formaVest.tekst}
                onChange={(e) => setFormaVest({ ...formaVest, tekst: e.target.value })}
                required
              />
            </div>
            <div className="auth-field">
              <label htmlFor="vest-datum">Датум</label>
              <input
                id="vest-datum"
                type="date"
                value={formaVest.datum}
                onChange={(e) => setFormaVest({ ...formaVest, datum: e.target.value })}
              />
            </div>
            <button type="submit" className="auth-submit" disabled={cuva}>
              {cuva ? 'Чување...' : 'Сачувај'}
            </button>
          </form>
        </Overlay>
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
