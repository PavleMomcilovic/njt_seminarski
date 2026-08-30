import { useEffect, useState } from 'react'
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import { AuthProvider, useAuth } from './context/AuthContext'
import { me } from './api/auth'
import Register from './pages/Register'
import Login from './pages/Login'
import Pocetna from './pages/Pocetna'

function RutePrijave() {
  const { osoba, setOsoba } = useAuth()
  const [ucitano, setUcitano] = useState(false)

  useEffect(() => {
    me()
      .then(setOsoba)
      .catch(() => {})
      .finally(() => setUcitano(true))
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  if (!ucitano) return null

  return (
    <Routes>
      <Route path="/registracija" element={<Register />} />
      <Route path="/login" element={<Login />} />
      <Route path="/pocetna" element={osoba ? <Pocetna /> : <Navigate to="/login" replace />} />
      <Route path="*" element={<Navigate to={osoba ? '/pocetna' : '/login'} replace />} />
    </Routes>
  )
}

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <RutePrijave />
      </AuthProvider>
    </BrowserRouter>
  )
}

export default App
