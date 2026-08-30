import client from './client'

export function extractErrorMessage(error) {
  const data = error?.response?.data
  if (!data) return 'Greška prilikom komunikacije sa serverom.'
  if (data.errors && typeof data.errors === 'object') {
    const prva = Object.values(data.errors)[0]
    if (prva) return prva
  }
  return data.message || 'Došlo je do greške.'
}

export async function register(osobaDto) {
  const response = await client.post('/osoba/registracija', osobaDto)
  return response.data.data.value
}

export async function login(email, sifra) {
  const response = await client.post('/osoba/login', { email, sifra })
  return response.data.data.value
}

export async function logout() {
  await client.post('/osoba/logout')
}

export async function me() {
  const response = await client.get('/osoba/me')
  return response.data.data.value
}

export async function getKatedre() {
  const response = await client.get('/katedre')
  return response.data.data.values
}

export async function getZvanja() {
  const response = await client.get('/zvanja')
  return response.data.data.values
}
