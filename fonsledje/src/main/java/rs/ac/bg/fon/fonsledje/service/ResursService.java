package rs.ac.bg.fon.fonsledje.service;

import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.fon.fonsledje.dto.ResursDto;
import rs.ac.bg.fon.fonsledje.entity.ResursId;

import java.util.List;

public interface ResursService {
    ResursDto addResurs(ResursDto resursDto, MultipartFile file);

    ResursDto getResurs(ResursId id);

    List<ResursDto> getAllResurs();

    ResursDto updateResurs(ResursId id, ResursDto resursDto, MultipartFile file);

    String deleteResurs(ResursId id);
}
