import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getOsobaById } from '../api/osobe'
import { getPredmeti } from '../api/predmeti'
import { getVerifikacijeZaStudenta, upisiOcenu } from '../api/verifikacije'
import { getKatedre, getZvanja, logout, extractErrorMessage } from '../api/auth'
import { useAuth } from '../context/AuthContext'
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
  const [izmene, setIzmene] = useState({})
  const [greska, setGreska] = useState('')

  const sopstveniProfil = ulogovan && Number(id) === ulogovan.idOsobe

  useEffect(() => {
    let otkazano = false

    async function ucitaj() {
      setOsoba(null)
      setVerifikacije([])
      const osobaPodaci = await getOsobaById(id)
      if (otkazano) return
      setOsoba(osobaPodaci)

      if (osobaPodaci.tip === 'STUDENT') {
        const v = await getVerifikacijeZaStudenta(osobaPodaci.idOsobe)
        if (!otkazano) setVerifikacije(v)
      } else if (osobaPodaci.tip === 'PROFESOR') {
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
          Одјави се
        </button>
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
        </section>
      )}
    </div>
  )
}
