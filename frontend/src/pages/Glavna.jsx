import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import Carousel from '../components/Carousel'
import Overlay from '../components/Overlay'
import { getVesti } from '../api/vesti'
import { getPredmeti } from '../api/predmeti'
import './Glavna.css'

const TEKST_LIMIT = 220

function formatDatum(datum) {
  if (!datum) return ''
  return new Date(datum).toLocaleDateString('sr-RS')
}

export default function Glavna() {
  const navigate = useNavigate()
  const [vesti, setVesti] = useState([])
  const [predmeti, setPredmeti] = useState([])
  const [otvorenaVest, setOtvorenaVest] = useState(null)

  useEffect(() => {
    getVesti().then(setVesti).catch(() => {})
    getPredmeti().then(setPredmeti).catch(() => {})
  }, [])

  return (
    <div>
      <section className="glavna-sekcija">
        <h2>Vesti</h2>
        {vesti.length === 0 ? (
          <p className="glavna-prazno">Trenutno nema vesti.</p>
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
                      Pročitaj više...
                    </button>
                  )}
                </div>
              )
            })}
          </Carousel>
        )}
      </section>

      <section className="glavna-sekcija">
        <h2>Predmeti</h2>
        {predmeti.length === 0 ? (
          <p className="glavna-prazno">Trenutno nema predmeta.</p>
        ) : (
          <Carousel>
            {predmeti.map((predmet) => (
              <div
                className="kartica predmet-kartica"
                key={predmet.idPredmeta}
                onClick={() => navigate(`/predmeti/${predmet.idPredmeta}`)}
              >
                <div className="kartica-naslov">{predmet.naziv}</div>
                <div className="kartica-datum">
                  {predmet.godina}. godina, {predmet.semestar}. semestar
                </div>
              </div>
            ))}
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
    </div>
  )
}
