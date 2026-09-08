import Overlay from './Overlay'
import './Potvrda.css'

export default function Potvrda({ poruka, onPotvrdi, onOdustani }) {
  return (
    <Overlay onClose={onOdustani}>
      <h2>Потврда</h2>
      <p>{poruka}</p>
      <div className="potvrda-dugmad">
        <button type="button" className="potvrda-da" onClick={onPotvrdi}>
          Да
        </button>
        <button type="button" className="potvrda-ne" onClick={onOdustani}>
          Не
        </button>
      </div>
    </Overlay>
  )
}
