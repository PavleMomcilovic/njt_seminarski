import client from './client'

export async function getTipoviResursa() {
  const response = await client.get('/tipovi-resursa')
  return response.data.data.values
}
