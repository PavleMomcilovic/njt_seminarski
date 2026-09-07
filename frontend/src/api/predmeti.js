import client from './client'

export async function getPredmeti() {
  const response = await client.get('/predmeti')
  return response.data.data.values
}

export async function getPredmetById(id) {
  const response = await client.get(`/predmeti/${id}`)
  return response.data.data.value
}
