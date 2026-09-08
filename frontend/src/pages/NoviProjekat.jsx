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

  function izaberiZipFajl(e) {
    setZipFajl(e.target.files[0] || null)
    e.target.value = ''
  }

  function dodajTekstualneFajlove(e) {
    const noviFajlovi = Array.from(e.target.files)
    setTekstualniFajlovi((prev) => [...prev, ...noviFajlovi])
    e.target.value = ''
  }

  function ukloniTekstualniFajl(indeks) {
    setTekstualniFajlovi((prev) => prev.filter((_, i) => i !== indeks))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setGreska('')

    if (!zipFajl) {
      setGreska('Зип датотека је обавезна.')
      return
    }
    if (tekstualniFajlovi.length === 0) {
      setGreska('Бар једна текстуална датотека је обавезна.')
      return
    }

    const idZip = nadjiTipResursa('zip')
    const idTekst = nadjiTipResursa('tekst')
    if (!idZip || !idTekst) {
      setGreska('Типови ресурса нису подешени у бази.')
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
      <h1>Нови пројекат{predmet ? ` — ${predmet.naziv}` : ''}</h1>

      {greska && <div className="auth-error">{greska}</div>}

      <form onSubmit={handleSubmit}>
        <div className="auth-field">
          <label htmlFor="naziv">Назив пројекта</label>
          <input id="naziv" type="text" value={naziv} onChange={(e) => setNaziv(e.target.value)} required />
        </div>

        <div className="auth-field">
          <label htmlFor="opis">Опис пројекта</label>
          <textarea id="opis" rows={5} value={opis} onChange={(e) => setOpis(e.target.value)} required />
        </div>

        <div className="auth-field">
          <label htmlFor="zip">Зип датотека</label>
          <input id="zip" type="file" accept=".zip,.rar,.7z,.tar,.gz" onChange={izaberiZipFajl} />
          {zipFajl && (
            <div className="izabrana-datoteka">
              <span>{zipFajl.name}</span>
              <button type="button" className="datoteka-obrisi" onClick={() => setZipFajl(null)}>
                Обриши
              </button>
            </div>
          )}
        </div>

        <div className="auth-field">
          <label htmlFor="tekst">Текстуалне датотеке</label>
          <input id="tekst" type="file" accept=".txt,.pdf,.doc,.docx" multiple onChange={dodajTekstualneFajlove} />
          {tekstualniFajlovi.map((fajl, indeks) => (
            <div className="izabrana-datoteka" key={`${fajl.name}-${indeks}`}>
              <span>{fajl.name}</span>
              <button type="button" className="datoteka-obrisi" onClick={() => ukloniTekstualniFajl(indeks)}>
                Обриши
              </button>
            </div>
          ))}
        </div>

        <button type="submit" className="auth-submit" disabled={ucitava}>
          {ucitava ? 'Постављање...' : 'Постави пројекат'}
        </button>
      </form>
    </div>
  )
}
