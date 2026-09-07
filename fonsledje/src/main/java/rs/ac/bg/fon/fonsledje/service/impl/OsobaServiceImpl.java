package rs.ac.bg.fon.fonsledje.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.fonsledje.converter.impl.OsobaConverter;
import rs.ac.bg.fon.fonsledje.dto.OsobaDto;
import rs.ac.bg.fon.fonsledje.dto.TipOsobe;
import rs.ac.bg.fon.fonsledje.entity.Osoba;
import rs.ac.bg.fon.fonsledje.entity.Student;
import rs.ac.bg.fon.fonsledje.exception.EntityNotFoundException;
import rs.ac.bg.fon.fonsledje.exception.ValidationException;
import rs.ac.bg.fon.fonsledje.repository.OsobaRepository;
import rs.ac.bg.fon.fonsledje.repository.ProjekatRepository;
import rs.ac.bg.fon.fonsledje.repository.VerifikacijaRepository;
import rs.ac.bg.fon.fonsledje.service.OsobaService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OsobaServiceImpl implements OsobaService {

    private final OsobaRepository osobaRepository;
    private final ProjekatRepository projekatRepository;
    private final VerifikacijaRepository verifikacijaRepository;
    private final OsobaConverter osobaConverter;
    private final PasswordEncoder passwordEncoder;

    public OsobaServiceImpl(OsobaRepository osobaRepository, ProjekatRepository projekatRepository,
                             VerifikacijaRepository verifikacijaRepository, OsobaConverter osobaConverter,
                             PasswordEncoder passwordEncoder) {
        this.osobaRepository = osobaRepository;
        this.projekatRepository = projekatRepository;
        this.verifikacijaRepository = verifikacijaRepository;
        this.osobaConverter = osobaConverter;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OsobaDto register(OsobaDto dto) {
        dto.setEmail(dto.getEmail() == null ? null : dto.getEmail().trim().toLowerCase());
        if (dto.getSifra() == null || dto.getSifra().length() < 9) {
            throw new ValidationException("Lozinka mora imati najmanje 9 karaktera.");
        }
        if (osobaRepository.findByEmail(dto.getEmail()) != null) {
            throw new ValidationException("Nalog sa email-om '" + dto.getEmail() + "' već postoji.");
        }
        if (dto.getTip() == TipOsobe.STUDENT) {
            if (dto.getBrojIndeksa() == null || dto.getBrojIndeksa().isBlank()) {
                throw new ValidationException("Broj indeksa je obavezan za studenta.");
            }
            if (dto.getStatus() == null) {
                throw new ValidationException("Status je obavezan za studenta.");
            }
        } else if (dto.getTip() == TipOsobe.PROFESOR) {
            if (dto.getIdKatedre() == null) {
                throw new ValidationException("Katedra je obavezna za profesora.");
            }
            if (dto.getIdZvanja() == null) {
                throw new ValidationException("Zvanje je obavezno za profesora.");
            }
        } else {
            throw new ValidationException("Tip osobe je obavezan.");
        }

        dto.setIdOsobe(null);
        dto.setSifra(passwordEncoder.encode(dto.getSifra()));
        Osoba osoba = osobaConverter.toEntity(dto);
        Osoba saved = osobaRepository.save(osoba);
        return osobaConverter.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OsobaDto findById(Long id) {
        Osoba osoba = osobaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Osoba sa ID " + id + " nije pronađena."));
        return osobaConverter.toDto(osoba);
    }

    @Override
    @Transactional(readOnly = true)
    public OsobaDto findByEmail(String email) {
        Osoba osoba = osobaRepository.findByEmail(email);
        if (osoba == null) {
            throw new EntityNotFoundException("Osoba sa email-om '" + email + "' nije pronađena.");
        }
        return osobaConverter.toDto(osoba);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OsobaDto> findAll() {
        return osobaRepository.findAll().stream()
                .map(osobaConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OsobaDto> search(String ime, String prezime) {
        return osobaRepository
                .findByImeContainingIgnoreCaseAndPrezimeContainingIgnoreCase(
                        ime == null ? "" : ime, prezime == null ? "" : prezime)
                .stream()
                .map(osobaConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OsobaDto updateCredentials(Long id, OsobaDto dto, Long currentUserId, boolean currentIsProfesor) {
        if (!id.equals(currentUserId)) {
            throw new AccessDeniedException("Kredencijale možete promeniti samo za sopstveni nalog.");
        }
        Osoba postojeca = osobaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Osoba sa ID " + id + " nije pronađena."));

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            String noviEmail = dto.getEmail().trim().toLowerCase();
            Osoba postojiSaEmailom = osobaRepository.findByEmail(noviEmail);
            if (postojiSaEmailom != null && !postojiSaEmailom.getIdOsobe().equals(id)) {
                throw new ValidationException("Nalog sa email-om '" + noviEmail + "' već postoji.");
            }
            postojeca.setEmail(noviEmail);
        }
        if (dto.getIme() != null && !dto.getIme().isBlank()) {
            postojeca.setIme(dto.getIme());
        }
        if (dto.getPrezime() != null && !dto.getPrezime().isBlank()) {
            postojeca.setPrezime(dto.getPrezime());
        }
        if (dto.getSifra() != null && !dto.getSifra().isBlank()) {
            if (dto.getSifra().length() < 9) {
                throw new ValidationException("Lozinka mora imati najmanje 9 karaktera.");
            }
            postojeca.setSifra(passwordEncoder.encode(dto.getSifra()));
        }

        if (postojeca instanceof Student s && dto.getTip() == TipOsobe.STUDENT) {
            if (dto.getBrojIndeksa() != null && !dto.getBrojIndeksa().isBlank()) {
                s.setBrojIndeksa(dto.getBrojIndeksa());
            }
            if (dto.getStatus() != null) {
                s.setStatus(dto.getStatus());
            }
        }

        Osoba saved = osobaRepository.save(postojeca);
        return osobaConverter.toDto(saved);
    }

    @Override
    public void delete(Long id, Long currentUserId, boolean currentIsProfesor) {
        Osoba osoba = osobaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Osoba sa ID " + id + " nije pronađena."));

        boolean sopstveniNalog = id.equals(currentUserId);
        boolean profesorBrisesStudenta = currentIsProfesor && osoba instanceof Student;
        if (!sopstveniNalog && !profesorBrisesStudenta) {
            throw new AccessDeniedException("Nemate dozvolu da obrišete ovaj nalog.");
        }

        if (osoba instanceof Student) {
            if (!projekatRepository.findByStudent_IdOsobe(id).isEmpty()) {
                throw new ValidationException("Nalog se ne može obrisati jer student ima postavljene projekte.");
            }
            verifikacijaRepository.deleteByStudent_IdOsobe(id);
        }

        osobaRepository.delete(osoba);
    }
}
