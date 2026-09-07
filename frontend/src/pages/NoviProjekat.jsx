import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getPredmetById } from '../api/predmeti'
import { createProjekat } from '../api/projekti'
import { createResurs } from '../api/resursi'
import { getTipoviResursa } from '../api/tipoviResursa'
import { extractErrorMessage } from '../api/auth'
import './Auth.css'
import './NoviProjekat.css'

export default function NoviProjekat() {
  const { id } = useParams()
  const navigate = useNavigate()

  const [predmet, setPredmet] = useState(null)
  const [tipoviResursa, setTipoviResursa] = useState([])
  const [naziv, setNaziv] = useState('')
  const [opis, setOpis] = useState('')
  const [zipFajl, setZipFajl] = useState(null)
  const [tekstualniFajlovi, setTekstualniFajlovi] = useState([])
  const [greska, setGreska] = useState('')
  const [ucitava, setUcitava] = useState(false)

  useEffect(() => {
    getPredmetById(id).then(setPredmet).catch(() => {})
    getTipoviResursa().then(setTipoviResursa).catch(() => {})
  }, [id])

  function nadjiTipResursa(kljucnaRec) {
    return tipoviResursa.find((t) => t.naziv.toLowerCase().includes(kljucnaRec))?.idTipResursa
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setGreska('')

    if (!zipFajl) {
      setGreska('Zip datoteka je obavezna.')
      return
    }
    if (tekstualniFajlovi.length === 0) {
      setGreska('Bar jedna tekstualna datoteka je obavezna.')
      return
    }

    const idZip = nadjiTipResursa('zip')
    const idTekst = nadjiTipResursa('tekst')
    if (!idZip || !idTekst) {
      setGreska('Tipovi resursa nisu podešeni u bazi.')
      return
    }

    setUcitava(true)
    try {
      const projekat = await createProjekat({ naziv, opis, idPredmeta: Number(id) })

      await createResurs(
        { naziv: zipFajl.name, opis, idProjekta: projekat.idProjekta, idTipaResursa: idZip },
        zipFajl
      )
      for (const fajl of tekstualniFajlovi) {
        await createResurs(
          { naziv: fajl.name, opis, idProjekta: projekat.idProjekta, idTipaResursa: idTekst },
          fajl
        )
      }

      navigate(`/predmeti/${id}`)
    } catch (err) {
      setGreska(extractErrorMessage(err))
    } finally {
      setUcitava(false)
    }
  }

  return (
    <div className="novi-projekat">
      <h1>Novi projekat{predmet ? ` — ${predmet.naziv}` : ''}</h1>

      {greska && <div className="auth-error">{greska}</div>}

      <form onSubmit={handleSubmit}>
        <div className="auth-field">
          <label htmlFor="naziv">Naziv projekta</label>
          <input id="naziv" type="text" value={naziv} onChange={(e) => setNaziv(e.target.value)} required />
        </div>

        <div className="auth-field">
          <label htmlFor="opis">Opis projekta</label>
          <textarea id="opis" rows={5} value={opis} onChange={(e) => setOpis(e.target.value)} required />
        </div>

        <div className="auth-field">
          <label htmlFor="zip">Zip datoteka</label>
          <input
            id="zip"
            type="file"
            accept=".zip"
            onChange={(e) => setZipFajl(e.target.files[0] || null)}
            required
          />
        </div>

        <div className="auth-field">
          <label htmlFor="tekst">Tekstualne datoteke</label>
          <input
            id="tekst"
            type="file"
            accept=".txt"
            multiple
            onChange={(e) => setTekstualniFajlovi(Array.from(e.target.files))}
            required
          />
        </div>

        <button type="submit" className="auth-submit" disabled={ucitava}>
          {ucitava ? 'Postavljanje...' : 'Postavi projekat'}
        </button>
      </form>
    </div>
  )
}
