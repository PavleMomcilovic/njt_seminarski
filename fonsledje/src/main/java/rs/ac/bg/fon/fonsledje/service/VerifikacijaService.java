package rs.ac.bg.fon.fonsledje.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ac.bg.fon.fonsledje.dto.VerifikacijaDto;

import java.util.List;

public interface VerifikacijaService {
    List<VerifikacijaDto> potvrdiPredmete(Long idStudenta, List<Long> idPredmeta);

    VerifikacijaDto upisiOcenu(Long idStudenta, Long idPredmeta, VerifikacijaDto dto, Long idProfesoraUlogovanog);

    VerifikacijaDto verifikujStudenta(Long idStudenta, Long idPredmeta, Long idProfesoraUlogovanog);

    Page<VerifikacijaDto> findByStudent(Long idStudenta, Pageable pageable);

    List<VerifikacijaDto> findAll();
}
