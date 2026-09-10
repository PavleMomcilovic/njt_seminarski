package rs.ac.bg.fon.fonsledje.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.fonsledje.converter.impl.VerifikacijaConverter;
import rs.ac.bg.fon.fonsledje.dto.VerifikacijaDto;
import rs.ac.bg.fon.fonsledje.entity.Predmet;
import rs.ac.bg.fon.fonsledje.entity.Profesor;
import rs.ac.bg.fon.fonsledje.entity.Student;
import rs.ac.bg.fon.fonsledje.entity.Verifikacija;
import rs.ac.bg.fon.fonsledje.entity.VerifikacijaId;
import rs.ac.bg.fon.fonsledje.exception.EntityNotFoundException;
import rs.ac.bg.fon.fonsledje.repository.PredmetRepository;
import rs.ac.bg.fon.fonsledje.repository.ProfesorRepository;
import rs.ac.bg.fon.fonsledje.repository.StudentRepository;
import rs.ac.bg.fon.fonsledje.repository.VerifikacijaRepository;
import rs.ac.bg.fon.fonsledje.service.VerifikacijaService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VerifikacijaServiceImpl implements VerifikacijaService {

    private final VerifikacijaRepository verifikacijaRepository;
    private final StudentRepository studentRepository;
    private final PredmetRepository predmetRepository;
    private final ProfesorRepository profesorRepository;
    private final VerifikacijaConverter verifikacijaConverter;

    public VerifikacijaServiceImpl(VerifikacijaRepository verifikacijaRepository, StudentRepository studentRepository,
                                    PredmetRepository predmetRepository, ProfesorRepository profesorRepository,
                                    VerifikacijaConverter verifikacijaConverter) {
        this.verifikacijaRepository = verifikacijaRepository;
        this.studentRepository = studentRepository;
        this.predmetRepository = predmetRepository;
        this.profesorRepository = profesorRepository;
        this.verifikacijaConverter = verifikacijaConverter;
    }

    @Override
    public List<VerifikacijaDto> potvrdiPredmete(Long idStudenta, List<Long> idPredmeta) {
        Student student = studentRepository.findById(idStudenta)
                .orElseThrow(() -> new EntityNotFoundException("Студент са ИД " + idStudenta + " није пронађен."));

        List<Verifikacija> kreirane = new ArrayList<>();
        for (Long idPredmet : idPredmeta) {
            VerifikacijaId id = new VerifikacijaId(idStudenta, idPredmet);
            if (verifikacijaRepository.existsById(id)) {
                continue;
            }
            Predmet predmet = predmetRepository.findById(idPredmet)
                    .orElseThrow(() -> new EntityNotFoundException("Предмет са ИД " + idPredmet + " није пронађен."));

            Verifikacija verifikacija = new Verifikacija();
            verifikacija.setId(id);
            verifikacija.setStatus(false);
            verifikacija.setStudent(student);
            verifikacija.setPredmet(predmet);
            kreirane.add(verifikacijaRepository.save(verifikacija));
        }
        return kreirane.stream().map(verifikacijaConverter::toDto).collect(Collectors.toList());
    }

    @Override
    public VerifikacijaDto upisiOcenu(Long idStudenta, Long idPredmeta, VerifikacijaDto dto, Long idProfesoraUlogovanog) {
        VerifikacijaId id = new VerifikacijaId(idStudenta, idPredmeta);
        Verifikacija verifikacija = verifikacijaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Студент није пријављен на предмет, верификација не постоји."));

        boolean predajePredmet = verifikacija.getPredmet().getProfesori().stream()
                .anyMatch(p -> p.getIdOsobe().equals(idProfesoraUlogovanog));
        if (!predajePredmet) {
            throw new AccessDeniedException("Можете оцењивати само студенте на предметима које предајете.");
        }
        Profesor profesor = profesorRepository.findById(idProfesoraUlogovanog)
                .orElseThrow(() -> new EntityNotFoundException("Професор са ИД " + idProfesoraUlogovanog + " није пронађен."));
        verifikacija.setProfesor(profesor);

        if (dto.getStatus() != null) {
            verifikacija.setStatus(dto.getStatus());
        }
        if (dto.getOcena() != null) {
            verifikacija.setOcena(dto.getOcena());
        }
        if (dto.getDatum() != null) {
            verifikacija.setDatum(dto.getDatum());
        }

        Verifikacija saved = verifikacijaRepository.save(verifikacija);
        return verifikacijaConverter.toDto(saved);
    }

    @Override
    public VerifikacijaDto verifikujStudenta(Long idStudenta, Long idPredmeta, Long idProfesoraUlogovanog) {
        Predmet predmet = predmetRepository.findById(idPredmeta)
                .orElseThrow(() -> new EntityNotFoundException("Предмет са ИД " + idPredmeta + " није пронађен."));
        boolean predajePredmet = predmet.getProfesori().stream()
                .anyMatch(p -> p.getIdOsobe().equals(idProfesoraUlogovanog));
        if (!predajePredmet) {
            throw new AccessDeniedException("Можете верификовати студенте само на предметима које предајете.");
        }
        Student student = studentRepository.findById(idStudenta)
                .orElseThrow(() -> new EntityNotFoundException("Студент са ИД " + idStudenta + " није пронађен."));
        Profesor profesor = profesorRepository.findById(idProfesoraUlogovanog)
                .orElseThrow(() -> new EntityNotFoundException("Професор са ИД " + idProfesoraUlogovanog + " није пронађен."));

        VerifikacijaId id = new VerifikacijaId(idStudenta, idPredmeta);
        Verifikacija verifikacija = verifikacijaRepository.findById(id).orElse(null);
        if (verifikacija == null) {
            verifikacija = new Verifikacija();
            verifikacija.setId(id);
            verifikacija.setStudent(student);
            verifikacija.setPredmet(predmet);
        }
        verifikacija.setStatus(true);
        verifikacija.setProfesor(profesor);

        Verifikacija saved = verifikacijaRepository.save(verifikacija);
        return verifikacijaConverter.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VerifikacijaDto> findByStudent(Long idStudenta, Pageable pageable) {
        return verifikacijaRepository.findByStudent_IdOsobe(idStudenta, pageable)
                .map(verifikacijaConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VerifikacijaDto> findAll() {
        return verifikacijaRepository.findAll().stream()
                .map(verifikacijaConverter::toDto)
                .collect(Collectors.toList());
    }
}
