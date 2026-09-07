import './Overlay.css'

export default function Overlay({ onClose, children }) {
  return (
    <div className="overlay-backdrop" onClick={onClose}>
      <div className="overlay-content" onClick={(e) => e.stopPropagation()}>
        <button type="button" className="overlay-close" onClick={onClose} aria-label="Затвори">
          ×
        </button>
        {children}
      </div>
    </div>
  )
}
