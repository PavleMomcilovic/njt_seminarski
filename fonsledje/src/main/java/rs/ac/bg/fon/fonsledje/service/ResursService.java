package rs.ac.bg.fon.fonsledje.service;

import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.fon.fonsledje.dto.ResursDto;
import rs.ac.bg.fon.fonsledje.entity.Resurs;
import rs.ac.bg.fon.fonsledje.entity.ResursId;

import java.util.List;

public interface ResursService {
    ResursDto addResurs(ResursDto resursDto, MultipartFile file, Long idStudentaUlogovanog);

    ResursDto getResurs(ResursId id);

    Resurs preuzmi(ResursId id);

    List<ResursDto> getAllResurs();

    List<ResursDto> getByProjekat(Long idProjekta);

    ResursDto updateResurs(ResursId id, ResursDto resursDto, MultipartFile file, Long currentUserId, boolean currentIsProfesor);

    String deleteResurs(ResursId id, Long currentUserId, boolean currentIsProfesor);
}
