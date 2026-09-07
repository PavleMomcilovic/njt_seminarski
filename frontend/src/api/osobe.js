import client from './client'

export async function getOsobe() {
  const response = await client.get('/osoba')
  return response.data.data.values
}

export async function getOsobaById(id) {
  const response = await client.get(`/osoba/${id}`)
  return response.data.data.value
}
