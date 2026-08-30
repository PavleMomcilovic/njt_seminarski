import { createContext, useContext, useState } from 'react'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [osoba, setOsoba] = useState(null)

  return (
    <AuthContext.Provider value={{ osoba, setOsoba }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
