package rs.ac.bg.fon.fonsledje.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.fonsledje.converter.impl.VestConverter;
import rs.ac.bg.fon.fonsledje.dto.VestDto;
import rs.ac.bg.fon.fonsledje.entity.Profesor;
import rs.ac.bg.fon.fonsledje.entity.Vest;
import rs.ac.bg.fon.fonsledje.entity.VestId;
import rs.ac.bg.fon.fonsledje.exception.EntityNotFoundException;
import rs.ac.bg.fon.fonsledje.exception.ValidationException;
import rs.ac.bg.fon.fonsledje.repository.ProfesorRepository;
import rs.ac.bg.fon.fonsledje.repository.VestRepository;
import rs.ac.bg.fon.fonsledje.service.VestService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VestServiceImpl implements VestService {

    private final VestRepository vestRepository;
    private final ProfesorRepository profesorRepository;
    private final VestConverter vestConverter;

    public VestServiceImpl(VestRepository vestRepository, ProfesorRepository profesorRepository, VestConverter vestConverter) {
        this.vestRepository = vestRepository;
        this.profesorRepository = profesorRepository;
        this.vestConverter = vestConverter;
    }

    @Override
    public VestDto create(VestDto dto, Long idProfesoraUlogovanog) {
        if (dto.getNaziv() == null || dto.getNaziv().isBlank()) {
            throw new ValidationException("Naziv vesti je obavezan.");
        }
        if (dto.getTekst() == null || dto.getTekst().isBlank()) {
            throw new ValidationException("Tekst vesti je obavezan.");
        }
        Profesor profesor = profesorRepository.findById(idProfesoraUlogovanog)
                .orElseThrow(() -> new EntityNotFoundException("Profesor sa ID " + idProfesoraUlogovanog + " nije pronađen."));

        Long maxId = vestRepository.findMaxIdVestiByProfesor(idProfesoraUlogovanog);
        long noviId = (maxId == null ? 0 : maxId) + 1;

        Vest vest = new Vest();
        vest.setId(new VestId(idProfesoraUlogovanog, noviId));
        vest.setNaziv(dto.getNaziv());
        vest.setTekst(dto.getTekst());
        vest.setDatum(dto.getDatum() != null ? dto.getDatum() : new java.util.Date());
        vest.setProfesor(profesor);

        Vest saved = vestRepository.save(vest);
        return vestConverter.toDto(saved);
    }

    @Override
    public VestDto update(VestId id, VestDto dto, Long currentUserId) {
        Vest vest = vestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vest nije pronađena."));
        if (!id.getIdProfesora().equals(currentUserId)) {
            throw new AccessDeniedException("Vest možete izmeniti samo ako ste njen autor.");
        }

        if (dto.getNaziv() != null && !dto.getNaziv().isBlank()) {
            vest.setNaziv(dto.getNaziv());
        }
        if (dto.getTekst() != null && !dto.getTekst().isBlank()) {
            vest.setTekst(dto.getTekst());
        }
        if (dto.getDatum() != null) {
            vest.setDatum(dto.getDatum());
        }

        Vest saved = vestRepository.save(vest);
        return vestConverter.toDto(saved);
    }

    @Override
    public void delete(VestId id) {
        Vest vest = vestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vest nije pronađena."));
        vestRepository.delete(vest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VestDto> findAll() {
        return vestRepository.findAll().stream()
                .map(vestConverter::toDto)
                .collect(Collectors.toList());
    }
}
