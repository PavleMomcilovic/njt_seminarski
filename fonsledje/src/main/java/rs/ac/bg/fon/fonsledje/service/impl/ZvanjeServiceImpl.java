package rs.ac.bg.fon.fonsledje.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.fonsledje.converter.impl.ZvanjeConverter;
import rs.ac.bg.fon.fonsledje.dto.ZvanjeDto;
import rs.ac.bg.fon.fonsledje.repository.ZvanjeRepository;
import rs.ac.bg.fon.fonsledje.service.ZvanjeService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ZvanjeServiceImpl implements ZvanjeService {

    private final ZvanjeRepository zvanjeRepository;
    private final ZvanjeConverter zvanjeConverter;

    public ZvanjeServiceImpl(ZvanjeRepository zvanjeRepository, ZvanjeConverter zvanjeConverter) {
        this.zvanjeRepository = zvanjeRepository;
        this.zvanjeConverter = zvanjeConverter;
    }

    @Override
    public List<ZvanjeDto> findAll() {
        return zvanjeRepository.findAll().stream()
                .map(zvanjeConverter::toDto)
                .collect(Collectors.toList());
    }
}
