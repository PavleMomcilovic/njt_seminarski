import client from './client'

export async function getVesti() {
  const response = await client.get('/vesti')
  return response.data.data.values
}
