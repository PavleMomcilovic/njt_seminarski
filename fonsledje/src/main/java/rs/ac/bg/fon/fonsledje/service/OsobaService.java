package rs.ac.bg.fon.fonsledje.service;

import rs.ac.bg.fon.fonsledje.dto.OsobaDto;

import java.util.List;

public interface OsobaService {
    OsobaDto register(OsobaDto dto);

    OsobaDto findById(Long id);

    OsobaDto findByEmail(String email);

    List<OsobaDto> findAll();

    List<OsobaDto> search(String ime, String prezime);

    OsobaDto updateCredentials(Long id, OsobaDto dto, Long currentUserId, boolean currentIsProfesor);

    void delete(Long id, Long currentUserId, boolean currentIsProfesor);
}
