import { useRef } from 'react'
import './Carousel.css'

export default function Carousel({ children }) {
  const trackRef = useRef(null)

  function scroll(offset) {
    trackRef.current?.scrollBy({ left: offset, behavior: 'smooth' })
  }

  return (
    <div className="carousel">
      <button type="button" className="carousel-arrow left" onClick={() => scroll(-320)} aria-label="Претходно">
        ‹
      </button>
      <div className="carousel-track" ref={trackRef}>
        {children}
      </div>
      <button type="button" className="carousel-arrow right" onClick={() => scroll(320)} aria-label="Следеће">
        ›
      </button>
    </div>
  )
}
