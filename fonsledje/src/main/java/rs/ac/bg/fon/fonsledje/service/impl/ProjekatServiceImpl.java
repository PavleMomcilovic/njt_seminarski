package rs.ac.bg.fon.fonsledje.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.fonsledje.converter.impl.ProjekatConverter;
import rs.ac.bg.fon.fonsledje.dto.ProjekatDto;
import rs.ac.bg.fon.fonsledje.entity.Predmet;
import rs.ac.bg.fon.fonsledje.entity.Projekat;
import rs.ac.bg.fon.fonsledje.entity.Student;
import rs.ac.bg.fon.fonsledje.entity.VerifikacijaId;
import rs.ac.bg.fon.fonsledje.exception.EntityNotFoundException;
import rs.ac.bg.fon.fonsledje.exception.ValidationException;
import rs.ac.bg.fon.fonsledje.repository.PredmetRepository;
import rs.ac.bg.fon.fonsledje.repository.ProjekatRepository;
import rs.ac.bg.fon.fonsledje.repository.StudentRepository;
import rs.ac.bg.fon.fonsledje.repository.VerifikacijaRepository;
import rs.ac.bg.fon.fonsledje.service.ProjekatService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjekatServiceImpl implements ProjekatService {

    private final ProjekatRepository projekatRepository;
    private final StudentRepository studentRepository;
    private final PredmetRepository predmetRepository;
    private final VerifikacijaRepository verifikacijaRepository;
    private final ProjekatConverter projekatConverter;

    public ProjekatServiceImpl(ProjekatRepository projekatRepository, StudentRepository studentRepository,
                                PredmetRepository predmetRepository, VerifikacijaRepository verifikacijaRepository,
                                ProjekatConverter projekatConverter) {
        this.projekatRepository = projekatRepository;
        this.studentRepository = studentRepository;
        this.predmetRepository = predmetRepository;
        this.verifikacijaRepository = verifikacijaRepository;
        this.projekatConverter = projekatConverter;
    }

    @Override
    public ProjekatDto create(ProjekatDto dto, Long idStudentaUlogovanog) {
        if (dto.getNaziv() == null || dto.getNaziv().isBlank()) {
            throw new ValidationException("Naziv projekta je obavezan.");
        }
        if (dto.getOpis() == null || dto.getOpis().isBlank()) {
            throw new ValidationException("Opis projekta je obavezan.");
        }
        if (dto.getIdPredmeta() == null) {
            throw new ValidationException("Predmet je obavezan.");
        }

        Student student = studentRepository.findById(idStudentaUlogovanog)
                .orElseThrow(() -> new EntityNotFoundException("Student sa ID " + idStudentaUlogovanog + " nije pronađen."));
        Predmet predmet = predmetRepository.findById(dto.getIdPredmeta())
                .orElseThrow(() -> new EntityNotFoundException("Predmet sa ID " + dto.getIdPredmeta() + " nije pronađen."));

        var verifikacija = verifikacijaRepository.findById(new VerifikacijaId(idStudentaUlogovanog, dto.getIdPredmeta()))
                .orElseThrow(() -> new ValidationException(
                        "Morate biti prijavljeni i verifikovani za ovaj predmet da biste postavili projekat."));
        if (!Boolean.TRUE.equals(verifikacija.getStatus())) {
            throw new ValidationException("Niste verifikovani za ovaj predmet, projekat se ne može postaviti.");
        }

        Projekat projekat = new Projekat();
        projekat.setNaziv(dto.getNaziv());
        projekat.setOpis(dto.getOpis());
        projekat.setStudent(student);
        projekat.setPredmet(predmet);

        Projekat saved = projekatRepository.save(projekat);
        return projekatConverter.toDto(saved);
    }

    @Override
    public ProjekatDto update(Long id, ProjekatDto dto, Long currentUserId, boolean currentIsProfesor) {
        Projekat projekat = projekatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projekat sa ID " + id + " nije pronađen."));
        proveriVlasnistvo(projekat, currentUserId, currentIsProfesor);

        if (dto.getNaziv() != null && !dto.getNaziv().isBlank()) {
            projekat.setNaziv(dto.getNaziv());
        }
        if (dto.getOpis() != null && !dto.getOpis().isBlank()) {
            projekat.setOpis(dto.getOpis());
        }

        Projekat saved = projekatRepository.save(projekat);
        return projekatConverter.toDto(saved);
    }

    @Override
    public void delete(Long id, Long currentUserId, boolean currentIsProfesor) {
        Projekat projekat = projekatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projekat sa ID " + id + " nije pronađen."));
        proveriVlasnistvo(projekat, currentUserId, currentIsProfesor);
        projekatRepository.delete(projekat);
    }

    private void proveriVlasnistvo(Projekat projekat, Long currentUserId, boolean currentIsProfesor) {
        boolean vlasnik = projekat.getStudent() != null && projekat.getStudent().getIdOsobe().equals(currentUserId);
        if (!vlasnik && !currentIsProfesor) {
            throw new AccessDeniedException("Nemate dozvolu da menjate ovaj projekat.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProjekatDto findById(Long id) {
        Projekat projekat = projekatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projekat sa ID " + id + " nije pronađen."));
        return projekatConverter.toDto(projekat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjekatDto> findAll() {
        return projekatRepository.findAll().stream()
                .map(projekatConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjekatDto> findByPredmet(Long idPredmeta) {
        return projekatRepository.findByPredmet_IdPredmeta(idPredmeta).stream()
                .map(projekatConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjekatDto> search(String naziv) {
        return projekatRepository.findByNazivContainingIgnoreCase(naziv == null ? "" : naziv).stream()
                .map(projekatConverter::toDto)
                .collect(Collectors.toList());
    }
}
