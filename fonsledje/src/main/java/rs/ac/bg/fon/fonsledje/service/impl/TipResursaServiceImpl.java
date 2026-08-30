package rs.ac.bg.fon.fonsledje.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.fonsledje.converter.impl.TipResursaConverter;
import rs.ac.bg.fon.fonsledje.dto.TipResursaDto;
import rs.ac.bg.fon.fonsledje.repository.TipResursaRepository;
import rs.ac.bg.fon.fonsledje.service.TipResursaService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TipResursaServiceImpl implements TipResursaService {

    private final TipResursaRepository tipResursaRepository;
    private final TipResursaConverter tipResursaConverter;

    public TipResursaServiceImpl(TipResursaRepository tipResursaRepository, TipResursaConverter tipResursaConverter) {
        this.tipResursaRepository = tipResursaRepository;
        this.tipResursaConverter = tipResursaConverter;
    }

    @Override
    public List<TipResursaDto> findAll() {
        return tipResursaRepository.findAll().stream()
                .map(tipResursaConverter::toDto)
                .collect(Collectors.toList());
    }
}
