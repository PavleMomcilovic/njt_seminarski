import { useEffect, useState, useMemo } from 'react'
import { useNavigate } from 'react-router-dom'
import { getOsobe } from '../api/osobe'
import './Zajednica.css'
import { odgovaraPretrazi } from '../utils/pretraga'

export default function Zajednica() {
  const navigate = useNavigate()
  const [osobe, setOsobe] = useState([])
  const [pretraga, setPretraga] = useState('')

  const filtriraneOsobe = useMemo(
    () => osobe.filter((o) => odgovaraPretrazi(`${o.ime} ${o.prezime}`, pretraga)),
    [osobe, pretraga]
  )

  useEffect(() => {
    getOsobe().then(setOsobe).catch(() => {})
  }, [])

  return (
    <div>
      <h1>Заједница</h1>
      <input
        type="text"
        className="pretraga-input"
        placeholder="Претражи особе..."
        value={pretraga}
        onChange={(e) => setPretraga(e.target.value)}
      />
      {filtriraneOsobe.length === 0 ? (
        <p>{osobe.length === 0 ? 'Тренутно нема особа.' : 'Нема особа које одговарају претрази.'}</p>
      ) : (
        <div className="osobe-lista">
        {filtriraneOsobe.map((osoba) => (
          <div className="osoba-kartica" key={osoba.idOsobe} onClick={() => navigate(`/osoba/${osoba.idOsobe}`)}>
            <div className="osoba-ime">
              {osoba.ime} {osoba.prezime}
            </div>
            <div className="osoba-tip">{osoba.tip === 'STUDENT' ? 'Студент' : 'Професор'}</div>
          </div>
        ))}
      </div>
      )}
    </div>
  )
}
