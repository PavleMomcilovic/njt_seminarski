const DIGRAFI = [
  ['nj', 'њ'],
  ['lj', 'љ'],
  ['dž', 'џ'],
]

const SLOVA = {
  a: 'а', b: 'б', v: 'в', g: 'г', d: 'д', đ: 'ђ', e: 'е', ž: 'ж', z: 'з',
  i: 'и', j: 'ј', k: 'к', l: 'л', m: 'м', n: 'н', o: 'о', p: 'п', r: 'р',
  s: 'с', t: 'т', ć: 'ћ', u: 'у', f: 'ф', h: 'х', c: 'ц', č: 'ч', š: 'ш',
}

export function latinicaUCirilicu(tekst) {
  const malo = tekst.toLowerCase()
  let rezultat = ''
  let i = 0
  while (i < malo.length) {
    const dva = malo.slice(i, i + 2)
    const digraf = DIGRAFI.find(([lat]) => lat === dva)
    if (digraf) {
      rezultat += digraf[1]
      i += 2
      continue
    }
    rezultat += SLOVA[malo[i]] || malo[i]
    i += 1
  }
  return rezultat
}

function escapeRegex(tekst) {
  return tekst.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
}

export function odgovaraPretrazi(naziv, upit) {
  const normalizovanUpit = latinicaUCirilicu(upit || '')
  if (!normalizovanUpit) return true
  const normalizovanNaziv = latinicaUCirilicu(naziv || '')
  const regex = new RegExp(escapeRegex(normalizovanUpit), 'i')
  return regex.test(normalizovanNaziv)
}
