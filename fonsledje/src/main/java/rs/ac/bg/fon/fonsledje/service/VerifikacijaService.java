package rs.ac.bg.fon.fonsledje.service;

import rs.ac.bg.fon.fonsledje.dto.VerifikacijaDto;

import java.util.List;

public interface VerifikacijaService {
    List<VerifikacijaDto> potvrdiPredmete(Long idStudenta, List<Long> idPredmeta);

    VerifikacijaDto upisiOcenu(Long idStudenta, Long idPredmeta, VerifikacijaDto dto, Long idProfesoraUlogovanog);

    List<VerifikacijaDto> findByStudent(Long idStudenta);

    List<VerifikacijaDto> findAll();
}
