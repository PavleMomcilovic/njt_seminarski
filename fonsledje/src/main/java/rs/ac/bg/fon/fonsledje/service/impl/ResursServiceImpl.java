package rs.ac.bg.fon.fonsledje.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.fon.fonsledje.converter.impl.ResursConverter;
import rs.ac.bg.fon.fonsledje.dto.ResursDto;
import rs.ac.bg.fon.fonsledje.entity.Projekat;
import rs.ac.bg.fon.fonsledje.entity.Resurs;
import rs.ac.bg.fon.fonsledje.entity.ResursId;
import rs.ac.bg.fon.fonsledje.entity.TipResursa;
import rs.ac.bg.fon.fonsledje.exception.EntityNotFoundException;
import rs.ac.bg.fon.fonsledje.exception.ValidationException;
import rs.ac.bg.fon.fonsledje.repository.ProjekatRepository;
import rs.ac.bg.fon.fonsledje.repository.ResursRepository;
import rs.ac.bg.fon.fonsledje.repository.TipResursaRepository;
import rs.ac.bg.fon.fonsledje.service.ResursService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ResursServiceImpl implements ResursService {

    private final ResursRepository resursRepository;
    private final ProjekatRepository projekatRepository;
    private final TipResursaRepository tipResursaRepository;
    private final ResursConverter resursConverter;

    public ResursServiceImpl(ResursRepository resursRepository, ProjekatRepository projekatRepository,
                              TipResursaRepository tipResursaRepository, ResursConverter resursConverter) {
        this.resursRepository = resursRepository;
        this.projekatRepository = projekatRepository;
        this.tipResursaRepository = tipResursaRepository;
        this.resursConverter = resursConverter;
    }

    @Override
    public ResursDto addResurs(ResursDto dto, MultipartFile file, Long idStudentaUlogovanog) {
        if (dto.getNaziv() == null || dto.getNaziv().isBlank()) {
            throw new ValidationException("Naziv resursa je obavezan.");
        }
        if (dto.getOpis() == null || dto.getOpis().isBlank()) {
            throw new ValidationException("Opis resursa je obavezan.");
        }
        if (dto.getIdProjekta() == null) {
            throw new ValidationException("Projekat je obavezan.");
        }
        if (dto.getIdTipaResursa() == null) {
            throw new ValidationException("Tip resursa je obavezan.");
        }
        if (file == null || file.isEmpty()) {
            throw new ValidationException("Fajl je obavezan.");
        }

        Projekat projekat = projekatRepository.findById(dto.getIdProjekta())
                .orElseThrow(() -> new EntityNotFoundException("Projekat sa ID " + dto.getIdProjekta() + " nije pronađen."));
        if (projekat.getStudent() == null || !projekat.getStudent().getIdOsobe().equals(idStudentaUlogovanog)) {
            throw new AccessDeniedException("Resurs možete dodati samo na sopstveni projekat.");
        }
        TipResursa tipResursa = tipResursaRepository.findById(dto.getIdTipaResursa())
                .orElseThrow(() -> new EntityNotFoundException("Tip resursa sa ID " + dto.getIdTipaResursa() + " nije pronađen."));

        Long maxId = resursRepository.findMaxIdResursaByProjekat(dto.getIdProjekta());
        long noviId = (maxId == null ? 0 : maxId) + 1;

        Resurs resurs = new Resurs();
        resurs.setId(new ResursId(dto.getIdProjekta(), noviId));
        resurs.setNaziv(dto.getNaziv());
        resurs.setOpis(dto.getOpis());
        resurs.setProjekat(projekat);
        resurs.setTipResursa(tipResursa);
        resurs.setVelicina(file.getSize());
        try {
            resurs.setSadrzaj(file.getBytes());
        } catch (IOException e) {
            throw new ValidationException("Greška prilikom čitanja fajla.");
        }

        Resurs saved = resursRepository.save(resurs);
        return resursConverter.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ResursDto getResurs(ResursId id) {
        Resurs resurs = resursRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resurs nije pronađen."));
        return resursConverter.toDto(resurs);
    }

    @Override
    @Transactional(readOnly = true)
    public Resurs preuzmi(ResursId id) {
        return resursRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resurs nije pronađen."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResursDto> getAllResurs() {
        return resursRepository.findAll().stream()
                .map(resursConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResursDto> getByProjekat(Long idProjekta) {
        return resursRepository.findByProjekat_IdProjekta(idProjekta).stream()
                .map(resursConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResursDto updateResurs(ResursId id, ResursDto dto, MultipartFile file, Long currentUserId, boolean currentIsProfesor) {
        Resurs resurs = resursRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resurs nije pronađen."));
        proveriVlasnistvo(resurs, currentUserId, currentIsProfesor);

        if (dto.getNaziv() != null && !dto.getNaziv().isBlank()) {
            resurs.setNaziv(dto.getNaziv());
        }
        if (dto.getOpis() != null && !dto.getOpis().isBlank()) {
            resurs.setOpis(dto.getOpis());
        }
        if (dto.getIdTipaResursa() != null) {
            TipResursa tipResursa = tipResursaRepository.findById(dto.getIdTipaResursa())
                    .orElseThrow(() -> new EntityNotFoundException("Tip resursa sa ID " + dto.getIdTipaResursa() + " nije pronađen."));
            resurs.setTipResursa(tipResursa);
        }
        if (file != null && !file.isEmpty()) {
            try {
                resurs.setSadrzaj(file.getBytes());
                resurs.setVelicina(file.getSize());
            } catch (IOException e) {
                throw new ValidationException("Greška prilikom čitanja fajla.");
            }
        }

        Resurs saved = resursRepository.save(resurs);
        return resursConverter.toDto(saved);
    }

    @Override
    public String deleteResurs(ResursId id, Long currentUserId, boolean currentIsProfesor) {
        Resurs resurs = resursRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resurs nije pronađen."));
        proveriVlasnistvo(resurs, currentUserId, currentIsProfesor);
        resursRepository.delete(resurs);
        return "Resurs uspešno obrisan.";
    }

    private void proveriVlasnistvo(Resurs resurs, Long currentUserId, boolean currentIsProfesor) {
        boolean vlasnik = resurs.getProjekat() != null && resurs.getProjekat().getStudent() != null
                && resurs.getProjekat().getStudent().getIdOsobe().equals(currentUserId);
        if (!vlasnik && !currentIsProfesor) {
            throw new AccessDeniedException("Nemate dozvolu da menjate ovaj resurs.");
        }
    }
}
