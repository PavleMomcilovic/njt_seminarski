import client from './client'

export async function getVesti() {
  const response = await client.get('/vesti')
  return response.data.data.values
}

export async function createVest(dto) {
  const response = await client.post('/vesti', dto)
  return response.data.data.value
}

export async function updateVest(idProfesora, idVesti, dto) {
  const response = await client.put(`/vesti/${idProfesora}/${idVesti}`, dto)
  return response.data.data.value
}

export async function deleteVest(idProfesora, idVesti) {
  await client.delete(`/vesti/${idProfesora}/${idVesti}`)
}
