import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { getProjekatById, deleteProjekat } from '../api/projekti'
import { getResursiByProjekat, preuzmiResursUrl } from '../api/resursi'
import { getPredmetById } from '../api/predmeti'
import { getOsobe } from '../api/osobe'
import { extractErrorMessage } from '../api/auth'
import { useAuth } from '../context/AuthContext'
import Potvrda from '../components/Potvrda'
import './Auth.css'
import './Predmeti.css'
import './ProjekatDetalji.css'

export default function ProjekatDetalji() {
  const { id } = useParams()
  const navigate = useNavigate()
  const { osoba } = useAuth()
  const [projekat, setProjekat] = useState(null)
  const [predmet, setPredmet] = useState(null)
  const [resursi, setResursi] = useState([])
  const [osobe, setOsobe] = useState([])
  const [greska, setGreska] = useState('')
  const [potvrda, setPotvrda] = useState(false)

  useEffect(() => {
    getProjekatById(id).then(setProjekat).catch(() => {})
    getResursiByProjekat(id).then(setResursi).catch(() => {})
    getOsobe().then(setOsobe).catch(() => {})
  }, [id])

  useEffect(() => {
    if (projekat?.idPredmeta) {
      getPredmetById(projekat.idPredmeta).then(setPredmet).catch(() => {})
    }
  }, [projekat])

  if (!projekat) return null

  const autor = osobe.find((o) => o.idOsobe === projekat.idStudenta)
  const jeVlasnik = osoba?.idOsobe === projekat.idStudenta
  const predajePredmet = predmet?.idProfesori?.includes(osoba?.idOsobe)
  const mozeDaObrise = jeVlasnik || (osoba?.tip === 'PROFESOR' && predajePredmet)

  async function obrisi() {
    setGreska('')
    try {
      await deleteProjekat(projekat.idProjekta)
      navigate(`/predmeti/${projekat.idPredmeta}`)
    } catch (err) {
      setGreska(extractErrorMessage(err))
    }
  }

  return (
    <div className="projekat-detalji">
      <div className="predmeti-naslov-red">
        <h1>{projekat.naziv}</h1>
        {mozeDaObrise && (
          <button type="button" className="predmet-obrisi" onClick={() => setPotvrda(true)}>
            Обриши
          </button>
        )}
      </div>

      {greska && <div className="auth-error">{greska}</div>}

      <p className="predmet-info">
        Аутор: {autor ? `${autor.ime} ${autor.prezime}` : `студент #${projekat.idStudenta}`}
      </p>
      {predmet && (
        <p className="predmet-info">
          Предмет: <Link to={`/predmeti/${predmet.idPredmeta}`}>{predmet.naziv}</Link>
        </p>
      )}

      <p className="projekat-opis">{projekat.opis}</p>

      <h2>Датотеке</h2>
      {resursi.length === 0 ? (
        <p>Нема постављених датотека.</p>
      ) : (
        <ul className="resursi-lista">
          {resursi.map((r) => (
            <li key={r.idResursa} className="resurs-red">
              <span>{r.naziv}</span>
              <a className="resurs-preuzmi" href={preuzmiResursUrl(projekat.idProjekta, r.idResursa)}>
                Преузми
              </a>
            </li>
          ))}
        </ul>
      )}

      {potvrda && (
        <Potvrda
          poruka="Обрисати овај пројекат?"
          onPotvrdi={() => {
            setPotvrda(false)
            obrisi()
          }}
          onOdustani={() => setPotvrda(false)}
        />
      )}
    </div>
  )
}
