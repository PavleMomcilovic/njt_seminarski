package rs.ac.bg.fon.fonsledje.service;

import rs.ac.bg.fon.fonsledje.dto.ProjekatDto;

import java.util.List;

public interface ProjekatService {
    ProjekatDto create(ProjekatDto dto, Long idStudentaUlogovanog);

    ProjekatDto update(Long id, ProjekatDto dto, Long currentUserId, boolean currentIsProfesor);

    void delete(Long id, Long currentUserId, boolean currentIsProfesor);

    ProjekatDto findById(Long id);

    List<ProjekatDto> findAll();

    List<ProjekatDto> findByPredmet(Long idPredmeta);

    List<ProjekatDto> search(String naziv);
}
