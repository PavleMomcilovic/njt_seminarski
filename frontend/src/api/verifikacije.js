import client from './client'

export async function getVerifikacijeZaStudenta(idStudenta, page = 0, size = 5) {
  const response = await client.get(`/verifikacije/student/${idStudenta}`, {
    params: { page, size, sort: 'datum,desc' }
  })
  return response.data.data
}

export async function upisiOcenu(idStudenta, idPredmeta, dto) {
  const response = await client.put(`/verifikacije/${idStudenta}/${idPredmeta}`, dto)
  return response.data.data.value
}

export async function verifikujStudenta(idStudenta, idPredmeta) {
  const response = await client.post(`/verifikacije/${idStudenta}/${idPredmeta}/verifikuj`)
  return response.data.data.value
}
