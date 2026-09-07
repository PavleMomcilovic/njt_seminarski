package rs.ac.bg.fon.fonsledje.service;

import rs.ac.bg.fon.fonsledje.dto.PredmetDto;

import java.util.List;

public interface PredmetService {
    PredmetDto create(PredmetDto dto, Long idProfesoraUlogovanog);

    PredmetDto prijaviSe(Long idPredmeta, Long idProfesora);

    PredmetDto update(Long id, PredmetDto dto);

    void delete(Long id);

    PredmetDto findById(Long id);

    List<PredmetDto> findAll();

    List<PredmetDto> search(String naziv);
}
