import client from './client'

export async function getVerifikacijeZaStudenta(idStudenta) {
  const response = await client.get(`/verifikacije/student/${idStudenta}`)
  return response.data.data.values
}

export async function upisiOcenu(idStudenta, idPredmeta, dto) {
  const response = await client.put(`/verifikacije/${idStudenta}/${idPredmeta}`, dto)
  return response.data.data.value
}
