import { useEffect, useMemo, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { FaCheckCircle, FaTimesCircle } from 'react-icons/fa'
import { getOsobaById } from '../api/osobe'
import { getPredmeti } from '../api/predmeti'
import { getVerifikacijeZaStudenta, upisiOcenu, verifikujStudenta } from '../api/verifikacije'
import { getKatedre, getZvanja, logout, extractErrorMessage } from '../api/auth'
import { odgovaraPretrazi } from '../utils/pretraga'
import { useAuth } from '../context/AuthContext'
import '../pages/Predmeti.css'
import './Profil.css'

const STATUS_NAZIVI = { AKTIVAN: 'Активан', APSOLVENT: 'Апсолвент', NEAKTIVAN: 'Неактиван' }

function formatDatum(datum) {
  return datum ? new Date(datum).toLocaleDateString('sr-RS') : '-'
}

export default function Profil() {
  const { id } = useParams()
  const navigate = useNavigate()
  const { osoba: ulogovan, setOsoba: setUlogovan } = useAuth()

  const [osoba, setOsoba] = useState(null)
  const [katedre, setKatedre] = useState([])
  const [zvanja, setZvanja] = useState([])
  const [predmeti, setPredmeti] = useState([])
  const [verifikacije, setVerifikacije] = useState([])
  const [stranica, setStranica] = useState(0)
  const [ukupnoStranica, setUkupnoStranica] = useState(0)
  const [izmene, setIzmene] = useState({})
  const [greska, setGreska] = useState('')
  const [pretragaPredmeta, setPretragaPredmeta] = useState('')

  const sopstveniProfil = ulogovan && Number(id) === ulogovan.idOsobe
  const gledaProfesor = osoba?.tip === 'STUDENT' && ulogovan?.tip === 'PROFESOR'

  const predmetiKojePredaje = useMemo(
    () => predmeti.filter((p) => p.idProfesori?.includes(ulogovan?.idOsobe)),
    [predmeti, ulogovan]
  )

  const filtriraniPredmetiZaVerifikaciju = useMemo(
    () => predmetiKojePredaje.filter((p) => odgovaraPretrazi(p.naziv, pretragaPredmeta)),
    [predmetiKojePredaje, pretragaPredmeta]
  )

  useEffect(() => {
    let otkazano = false
    setStranica(0)

    async function ucitaj() {
      setOsoba(null)
      setVerifikacije([])
      setUkupnoStranica(0)
      const osobaPodaci = await getOsobaById(id)
      if (otkazano) return
      setOsoba(osobaPodaci)

      if (osobaPodaci.tip === 'PROFESOR') {
        const [k, z] = await Promise.all([getKatedre(), getZvanja()])
        if (!otkazano) {
          setKatedre(k)
          setZvanja(z)
        }
      }
    }

    ucitaj().catch(() => {})
    getPredmeti().then((p) => !otkazano && setPredmeti(p)).catch(() => {})

    return () => {
      otkazano = true
    }
  }, [id])

  useEffect(() => {
    if (osoba?.tip !== 'STUDENT') return
    let otkazano = false

    getVerifikacijeZaStudenta(osoba.idOsobe, stranica, 5)
      .then((v) => {
        if (otkazano) return
        setVerifikacije(v.values)
        setUkupnoStranica(v.totalPages)
      })
      .catch(() => {})

    return () => {
      otkazano = true
    }
  }, [osoba, stranica])

  async function handleLogout() {
    await logout()
    setUlogovan(null)
    navigate('/login')
  }

  function nazivPredmeta(idPredmeta) {
    return predmeti.find((p) => p.idPredmeta === idPredmeta)?.naziv || `Предмет #${idPredmeta}`
  }

  function postaviIzmenu(idPredmeta, polje, vrednost) {
    setIzmene((prev) => ({
      ...prev,
      [idPredmeta]: { ...prev[idPredmeta], [polje]: vrednost },
    }))
  }

  async function verifikujZaPredmet(idPredmeta) {
    setGreska('')
    try {
      const azurirano = await verifikujStudenta(osoba.idOsobe, idPredmeta)
      setVerifikacije((prev) =>
        prev.some((v) => v.idPredmeta === idPredmeta)
          ? prev.map((v) => (v.idPredmeta === idPredmeta ? azurirano : v))
          : [...prev, azurirano]
      )
    } catch (err) {
      setGreska(extractErrorMessage(err))
    }
  }

  async function sacuvajVerifikaciju(v) {
    setGreska('')
    const izmena = izmene[v.idPredmeta] || {}
    try {
      const dto = {
        status: izmena.status !== undefined ? izmena.status : v.status,
        ocena: izmena.ocena !== undefined ? Number(izmena.ocena) : v.ocena,
        datum: izmena.datum !== undefined ? izmena.datum : v.datum,
      }
      const azurirano = await upisiOcenu(v.idStudenta, v.idPredmeta, dto)
      setVerifikacije((prev) => prev.map((row) => (row.idPredmeta === v.idPredmeta ? azurirano : row)))
    } catch (err) {
      setGreska(extractErrorMessage(err))
    }
  }

  if (!osoba) return null

  return (
    <div className="profil">
      <h1>
        {osoba.ime} {osoba.prezime}
      </h1>
      <p className="profil-red">Имејл: {osoba.email}</p>
      <p className="profil-red">Тип: {osoba.tip === 'STUDENT' ? 'Студент' : 'Професор'}</p>

      {osoba.tip === 'STUDENT' && (
        <>
          <p className="profil-red">Број индекса: {osoba.brojIndeksa}</p>
          <p className="profil-red">Статус: {STATUS_NAZIVI[osoba.status] || osoba.status}</p>
        </>
      )}

      {osoba.tip === 'PROFESOR' && (
        <>
          <p className="profil-red">
            Катедра: {katedre.find((k) => k.idKatedre === osoba.idKatedre)?.naziv || '-'}
          </p>
          <p className="profil-red">
            Звање: {zvanja.find((z) => z.idZvanja === osoba.idZvanja)?.naziv || '-'}
          </p>
        </>
      )}

      {sopstveniProfil && (
        <button type="button" className="profil-logout" onClick={handleLogout}>
          Одјава
        </button>
      )}

      {gledaProfesor && (
        <section className="profil-predmeti">
          <h2>Верификујте студента за предмет</h2>
          <input
            type="text"
            className="pretraga-input"
            placeholder="Претражите предмете..."
            value={pretragaPredmeta}
            onChange={(e) => setPretragaPredmeta(e.target.value)}
          />
          {filtriraniPredmetiZaVerifikaciju.length === 0 ? (
            <p>
              {predmetiKojePredaje.length === 0
                ? 'Не предајете ниједан предмет.'
                : 'Нема предмета који одговарају претрази.'}
            </p>
          ) : (
            <ul className="verifikacija-predmeti-lista">
              {filtriraniPredmetiZaVerifikaciju.map((p) => {
                const vecVerifikovan = verifikacije.some((v) => v.idPredmeta === p.idPredmeta && v.status)
                return (
                  <li key={p.idPredmeta} className="verifikacija-predmet-red">
                    <span>{p.naziv}</span>
                    <button
                      type="button"
                      className={`predmet-predaje-toggle ${vecVerifikovan ? 'predaje' : 'ne-predaje'}`}
                      onClick={() => verifikujZaPredmet(p.idPredmeta)}
                      title={vecVerifikovan ? 'Студент је верификован за овај предмет' : 'Кликните за верификацију студента за овај предмет'}
                      aria-label={vecVerifikovan ? 'Студент верификован' : 'Верификуј студента за предмет'}
                    >
                      {vecVerifikovan ? <FaCheckCircle size={18} /> : <FaTimesCircle size={18} />}
                    </button>
                  </li>
                )
              })}
            </ul>
          )}
        </section>
      )}

      {osoba.tip === 'STUDENT' && (
        <section className="profil-predmeti">
          <h2>Предмети</h2>
          {greska && <div className="profil-greska">{greska}</div>}
          {verifikacije.length === 0 ? (
            <p>Студент није пријављен ни на један предмет.</p>
          ) : (
            <table className="verifikacije-tabela">
              <thead>
                <tr>
                  <th>Предмет</th>
                  <th>Статус</th>
                  <th>Оцена</th>
                  <th>Датум</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {verifikacije.map((v) => {
                  const predajePredmet = predmeti
                    .find((p) => p.idPredmeta === v.idPredmeta)
                    ?.idProfesori?.includes(ulogovan?.idOsobe)
                  const mozeDaVerifikuje = ulogovan?.tip === 'PROFESOR' && predajePredmet
                  const izmena = izmene[v.idPredmeta] || {}
                  return (
                    <tr key={v.idPredmeta}>
                      <td>{nazivPredmeta(v.idPredmeta)}</td>
                      <td>
                        {mozeDaVerifikuje ? (
                          <input
                            type="checkbox"
                            checked={izmena.status !== undefined ? izmena.status : v.status}
                            onChange={(e) => postaviIzmenu(v.idPredmeta, 'status', e.target.checked)}
                          />
                        ) : v.status ? (
                          'Верификован'
                        ) : (
                          'Није верификован'
                        )}
                      </td>
                      <td>
                        {mozeDaVerifikuje ? (
                          <input
                            type="number"
                            className="verifikacija-input"
                            value={izmena.ocena !== undefined ? izmena.ocena : v.ocena || ''}
                            onChange={(e) => postaviIzmenu(v.idPredmeta, 'ocena', e.target.value)}
                          />
                        ) : (
                          v.ocena ?? '-'
                        )}
                      </td>
                      <td>
                        {mozeDaVerifikuje ? (
                          <input
                            type="date"
                            className="verifikacija-input"
                            value={izmena.datum !== undefined ? izmena.datum : v.datum || ''}
                            onChange={(e) => postaviIzmenu(v.idPredmeta, 'datum', e.target.value)}
                          />
                        ) : (
                          formatDatum(v.datum)
                        )}
                      </td>
                      <td>
                        {mozeDaVerifikuje && (
                          <button type="button" className="verifikacija-sacuvaj" onClick={() => sacuvajVerifikaciju(v)}>
                            Сачувај
                          </button>
                        )}
                      </td>
                    </tr>
                  )
                })}
              </tbody>
            </table>
          )}
          {ukupnoStranica > 1 && (
            <div className="paginacija">
              <button
                type="button"
                className="paginacija-strelica"
                disabled={stranica === 0}
                onClick={() => setStranica((s) => s - 1)}
                aria-label="Претходна страна"
              >
                &#8592;
              </button>
              {Array.from({ length: ukupnoStranica }, (_, br) => br).map((br) => (
                <button
                  key={br}
                  type="button"
                  className={`paginacija-broj ${br === stranica ? 'aktivna' : ''}`}
                  onClick={() => setStranica(br)}
                >
                  {br + 1}
                </button>
              ))}
              <button
                type="button"
                className="paginacija-strelica"
                disabled={stranica + 1 >= ukupnoStranica}
                onClick={() => setStranica((s) => s + 1)}
                aria-label="Следећа страна"
              >
                &#8594;
              </button>
            </div>
          )}
        </section>
      )}
    </div>
  )
}
