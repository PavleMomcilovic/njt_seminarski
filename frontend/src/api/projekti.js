import client from './client'

export async function getProjektiByPredmet(idPredmeta) {
  const response = await client.get(`/projekti/predmet/${idPredmeta}`)
  return response.data.data.values
}

export async function createProjekat(dto) {
  const response = await client.post('/projekti', dto)
  return response.data.data.value
}
