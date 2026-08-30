package rs.ac.bg.fon.fonsledje.service;

import rs.ac.bg.fon.fonsledje.dto.VestDto;
import rs.ac.bg.fon.fonsledje.entity.VestId;

import java.util.List;

public interface VestService {
    VestDto create(VestDto dto, Long idProfesoraUlogovanog);

    VestDto update(VestId id, VestDto dto, Long currentUserId);

    void delete(VestId id);

    List<VestDto> findAll();
}
