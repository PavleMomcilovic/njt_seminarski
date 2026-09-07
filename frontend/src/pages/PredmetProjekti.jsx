import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { getPredmetById } from '../api/predmeti'
import { getProjektiByPredmet } from '../api/projekti'
import { getVerifikacijeZaStudenta } from '../api/verifikacije'
import { useAuth } from '../context/AuthContext'
import './Predmeti.css'

export default function PredmetProjekti() {
  const { id } = useParams()
  const { osoba } = useAuth()
  const [predmet, setPredmet] = useState(null)
  const [projekti, setProjekti] = useState([])
  const [verifikovan, setVerifikovan] = useState(false)

  useEffect(() => {
    getPredmetById(id).then(setPredmet).catch(() => {})
    getProjektiByPredmet(id).then(setProjekti).catch(() => {})

    if (osoba?.tip === 'STUDENT') {
      getVerifikacijeZaStudenta(osoba.idOsobe)
        .then((verifikacije) =>
          setVerifikovan(verifikacije.some((v) => v.idPredmeta === Number(id) && v.status))
        )
        .catch(() => {})
    }
  }, [id, osoba])

  return (
    <div>
      <h1>{predmet ? predmet.naziv : 'Predmet'}</h1>

      {verifikovan && (
        <Link to={`/predmeti/${id}/novi-projekat`} className="dodaj-projekat-dugme">
          + Dodaj projekat
        </Link>
      )}

      {projekti.length === 0 ? (
        <p>Za ovaj predmet još nema postavljenih projekata.</p>
      ) : (
        <div className="predmeti-grid">
          {projekti.map((projekat) => (
            <div className="predmet-kartica" key={projekat.idProjekta}>
              <div className="predmet-naziv">{projekat.naziv}</div>
              <div className="predmet-info">{projekat.opis}</div>
              <Link to={`/osoba/${projekat.idStudenta}`} className="predmet-info">
                Autor: student #{projekat.idStudenta}
              </Link>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
