import client from './client'

export async function getPredmeti() {
  const response = await client.get('/predmeti')
  return response.data.data.values
}

export async function getPredmetById(id) {
  const response = await client.get(`/predmeti/${id}`)
  return response.data.data.value
}

export async function createPredmet(dto) {
  const response = await client.post(`/predmeti`, dto)
  return response.data.data.value
}

export async function updatePredmet(idPredmeta, dto) {
  const response = await client.put(`/predmeti/${idPredmeta}`, dto)
  return response.data.data.value
}

export async function deletePredmet(idPredmeta) {
  await client.delete(`/predmeti/${idPredmeta}`)
}

export async function prijaviSeZaPredmet(idPredmeta) {
  const response = await client.post(`/predmeti/${idPredmeta}/predajem`)
  return response.data.data.value
}

export async function odjaviSeZaPredmet(idPredmeta) {
  const response = await client.delete(`/predmeti/${idPredmeta}/predajem`)
  return response.data.data.value
}