package rs.ac.bg.fon.fonsledje.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.fonsledje.converter.impl.PredmetConverter;
import rs.ac.bg.fon.fonsledje.dto.PredmetDto;
import rs.ac.bg.fon.fonsledje.entity.Predmet;
import rs.ac.bg.fon.fonsledje.entity.Profesor;
import rs.ac.bg.fon.fonsledje.exception.EntityNotFoundException;
import rs.ac.bg.fon.fonsledje.exception.ValidationException;
import rs.ac.bg.fon.fonsledje.repository.PredmetRepository;
import rs.ac.bg.fon.fonsledje.repository.ProfesorRepository;
import rs.ac.bg.fon.fonsledje.service.PredmetService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PredmetServiceImpl implements PredmetService {

    private final PredmetRepository predmetRepository;
    private final ProfesorRepository profesorRepository;
    private final PredmetConverter predmetConverter;

    public PredmetServiceImpl(PredmetRepository predmetRepository, ProfesorRepository profesorRepository,
                               PredmetConverter predmetConverter) {
        this.predmetRepository = predmetRepository;
        this.profesorRepository = profesorRepository;
        this.predmetConverter = predmetConverter;
    }

    @Override
    public PredmetDto create(PredmetDto dto, Long idProfesoraUlogovanog) {
        if (dto.getNaziv() == null || dto.getNaziv().isBlank()) {
            throw new ValidationException("Назив предмета је обавезан.");
        }
        if (dto.getGodina() == null) {
            throw new ValidationException("Година је обавезна.");
        }
        if (dto.getSemestar() == null) {
            throw new ValidationException("Семестар је обавезан.");
        }
        Profesor profesor = profesorRepository.findById(idProfesoraUlogovanog)
                .orElseThrow(() -> new EntityNotFoundException("Професор са ИД " + idProfesoraUlogovanog + " није пронађен."));

        Predmet predmet = new Predmet();
        predmet.setNaziv(dto.getNaziv());
        predmet.setGodina(dto.getGodina());
        predmet.setSemestar(dto.getSemestar());
        predmet.getProfesori().add(profesor);

        Predmet saved = predmetRepository.save(predmet);
        return predmetConverter.toDto(saved);
    }

    @Override
    public PredmetDto prijaviSe(Long idPredmeta, Long idProfesora) {
        Predmet predmet = predmetRepository.findById(idPredmeta)
                .orElseThrow(() -> new EntityNotFoundException("Предмет са ИД " + idPredmeta + " није пронађен."));
        Profesor profesor = profesorRepository.findById(idProfesora)
                .orElseThrow(() -> new EntityNotFoundException("Професор са ИД " + idProfesora + " није пронађен."));

        predmet.getProfesori().add(profesor);

        Predmet saved = predmetRepository.save(predmet);
        return predmetConverter.toDto(saved);
    }

    @Override
    public PredmetDto odjaviSe(Long idPredmeta, Long idProfesora) {
        Predmet predmet = predmetRepository.findById(idPredmeta)
                .orElseThrow(() -> new EntityNotFoundException("Предмет са ИД " + idPredmeta + " није пронађен."));
        Profesor profesor = profesorRepository.findById(idProfesora)
                .orElseThrow(() -> new EntityNotFoundException("Професор са ИД " + idProfesora + " није пронађен."));

        predmet.getProfesori().remove(profesor);

        Predmet saved = predmetRepository.save(predmet);
        return predmetConverter.toDto(saved);
    }

    @Override
    public PredmetDto update(Long id, PredmetDto dto) {
        Predmet predmet = predmetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Предмет са ИД " + id + " није пронађен."));

        if (dto.getNaziv() != null && !dto.getNaziv().isBlank()) {
            predmet.setNaziv(dto.getNaziv());
        }
        if (dto.getGodina() != null) {
            predmet.setGodina(dto.getGodina());
        }
        if (dto.getSemestar() != null) {
            predmet.setSemestar(dto.getSemestar());
        }

        Predmet saved = predmetRepository.save(predmet);
        return predmetConverter.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        Predmet predmet = predmetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Предмет са ИД " + id + " није пронађен."));
        predmetRepository.delete(predmet);
    }

    @Override
    @Transactional(readOnly = true)
    public PredmetDto findById(Long id) {
        Predmet predmet = predmetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Предмет са ИД " + id + " није пронађен."));
        return predmetConverter.toDto(predmet);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PredmetDto> findAll() {
        return predmetRepository.findAll().stream()
                .map(predmetConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PredmetDto> search(String naziv) {
        return predmetRepository.findByNazivContainingIgnoreCase(naziv == null ? "" : naziv).stream()
                .map(predmetConverter::toDto)
                .collect(Collectors.toList());
    }
}
