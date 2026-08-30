package rs.ac.bg.fon.fonsledje.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.fonsledje.converter.impl.VerifikacijaConverter;
import rs.ac.bg.fon.fonsledje.dto.VerifikacijaDto;
import rs.ac.bg.fon.fonsledje.entity.Predmet;
import rs.ac.bg.fon.fonsledje.entity.Student;
import rs.ac.bg.fon.fonsledje.entity.Verifikacija;
import rs.ac.bg.fon.fonsledje.entity.VerifikacijaId;
import rs.ac.bg.fon.fonsledje.exception.EntityNotFoundException;
import rs.ac.bg.fon.fonsledje.repository.PredmetRepository;
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
    private final VerifikacijaConverter verifikacijaConverter;

    public VerifikacijaServiceImpl(VerifikacijaRepository verifikacijaRepository, StudentRepository studentRepository,
                                    PredmetRepository predmetRepository, VerifikacijaConverter verifikacijaConverter) {
        this.verifikacijaRepository = verifikacijaRepository;
        this.studentRepository = studentRepository;
        this.predmetRepository = predmetRepository;
        this.verifikacijaConverter = verifikacijaConverter;
    }

    @Override
    public List<VerifikacijaDto> potvrdiPredmete(Long idStudenta, List<Long> idPredmeta) {
        Student student = studentRepository.findById(idStudenta)
                .orElseThrow(() -> new EntityNotFoundException("Student sa ID " + idStudenta + " nije pronađen."));

        List<Verifikacija> kreirane = new ArrayList<>();
        for (Long idPredmet : idPredmeta) {
            VerifikacijaId id = new VerifikacijaId(idStudenta, idPredmet);
            if (verifikacijaRepository.existsById(id)) {
                continue;
            }
            Predmet predmet = predmetRepository.findById(idPredmet)
                    .orElseThrow(() -> new EntityNotFoundException("Predmet sa ID " + idPredmet + " nije pronađen."));

            Verifikacija verifikacija = new Verifikacija();
            verifikacija.setId(id);
            verifikacija.setStatus(false);
            verifikacija.setStudent(student);
            verifikacija.setPredmet(predmet);
            verifikacija.setProfesor(predmet.getProfesorOdobrio());
            kreirane.add(verifikacijaRepository.save(verifikacija));
        }
        return kreirane.stream().map(verifikacijaConverter::toDto).collect(Collectors.toList());
    }

    @Override
    public VerifikacijaDto upisiOcenu(Long idStudenta, Long idPredmeta, VerifikacijaDto dto) {
        VerifikacijaId id = new VerifikacijaId(idStudenta, idPredmeta);
        Verifikacija verifikacija = verifikacijaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Student nije prijavljen na predmet, verifikacija ne postoji."));

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
    @Transactional(readOnly = true)
    public List<VerifikacijaDto> findByStudent(Long idStudenta) {
        return verifikacijaRepository.findByStudent_IdOsobe(idStudenta).stream()
                .map(verifikacijaConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VerifikacijaDto> findAll() {
        return verifikacijaRepository.findAll().stream()
                .map(verifikacijaConverter::toDto)
                .collect(Collectors.toList());
    }
}
