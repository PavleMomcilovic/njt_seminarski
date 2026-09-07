import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getPredmeti } from '../api/predmeti'
import './Predmeti.css'

export default function Predmeti() {
  const navigate = useNavigate()
  const [predmeti, setPredmeti] = useState([])

  useEffect(() => {
    getPredmeti().then(setPredmeti).catch(() => {})
  }, [])

  return (
    <div>
      <h1>Predmeti</h1>
      {predmeti.length === 0 ? (
        <p>Trenutno nema predmeta.</p>
      ) : (
        <div className="predmeti-grid">
          {predmeti.map((predmet) => (
            <div
              className="predmet-kartica"
              key={predmet.idPredmeta}
              onClick={() => navigate(`/predmeti/${predmet.idPredmeta}`)}
            >
              <div className="predmet-naziv">{predmet.naziv}</div>
              <div className="predmet-info">
                {predmet.godina}. godina, {predmet.semestar}. semestar
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
