import { useEffect, useState } from 'react'
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import { AuthProvider, useAuth } from './context/AuthContext'
import { me } from './api/auth'
import Layout from './components/Layout'
import Register from './pages/Register'
import Login from './pages/Login'
import Glavna from './pages/Glavna'
import Predmeti from './pages/Predmeti'
import PredmetProjekti from './pages/PredmetProjekti'
import NoviProjekat from './pages/NoviProjekat'
import Zajednica from './pages/Zajednica'
import Profil from './pages/Profil'

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

  if (!osoba) {
    return (
      <Routes>
        <Route path="/registracija" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    )
  }

  return (
    <Routes>
      <Route path="/registracija" element={<Navigate to="/glavna" replace />} />
      <Route path="/login" element={<Navigate to="/glavna" replace />} />
      <Route element={<Layout />}>
        <Route path="/glavna" element={<Glavna />} />
        <Route path="/predmeti" element={<Predmeti />} />
        <Route path="/predmeti/:id" element={<PredmetProjekti />} />
        <Route path="/predmeti/:id/novi-projekat" element={<NoviProjekat />} />
        <Route path="/zajednica" element={<Zajednica />} />
        <Route path="/osoba/:id" element={<Profil />} />
      </Route>
      <Route path="*" element={<Navigate to="/glavna" replace />} />
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
