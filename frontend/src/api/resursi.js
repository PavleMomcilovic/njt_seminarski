import client from './client'

export async function getResursiByProjekat(idProjekta) {
  const response = await client.get(`/resursi/projekat/${idProjekta}`)
  return response.data.data.values
}

export function preuzmiResursUrl(idProjekta, idResursa) {
  return `${client.defaults.baseURL}/resursi/${idProjekta}/${idResursa}/preuzmi`
}

export async function createResurs(dto, file) {
  const formData = new FormData()
  formData.append('resurs', new Blob([JSON.stringify(dto)], { type: 'application/json' }))
  formData.append('file', file)
  const response = await client.post('/resursi', formData)
  return response.data.data.value
}
