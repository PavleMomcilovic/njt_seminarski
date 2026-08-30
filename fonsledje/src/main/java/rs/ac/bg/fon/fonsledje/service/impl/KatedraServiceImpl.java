package rs.ac.bg.fon.fonsledje.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.fonsledje.converter.impl.KatedraConverter;
import rs.ac.bg.fon.fonsledje.dto.KatedraDto;
import rs.ac.bg.fon.fonsledje.repository.KatedraRepository;
import rs.ac.bg.fon.fonsledje.service.KatedraService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class KatedraServiceImpl implements KatedraService {

    private final KatedraRepository katedraRepository;
    private final KatedraConverter katedraConverter;

    public KatedraServiceImpl(KatedraRepository katedraRepository, KatedraConverter katedraConverter) {
        this.katedraRepository = katedraRepository;
        this.katedraConverter = katedraConverter;
    }

    @Override
    public List<KatedraDto> findAll() {
        return katedraRepository.findAll().stream()
                .map(katedraConverter::toDto)
                .collect(Collectors.toList());
    }
}
