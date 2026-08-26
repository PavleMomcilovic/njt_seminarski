package rs.ac.bg.fon.fonsledje.converter.impl;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.fonsledje.converter.Converter;
import rs.ac.bg.fon.fonsledje.dto.TipResursaDto;
import rs.ac.bg.fon.fonsledje.entity.TipResursa;

@Component
public class TipResursaConverter implements Converter<TipResursaDto, TipResursa> {

    @Override
    public TipResursa toEntity(TipResursaDto dto) {
        if (dto == null) return null;
        TipResursa t = new TipResursa();
        t.setIdTipaResursa(dto.getIdTipResursa());
        t.setNaziv(dto.getNaziv());
        return t;
    }

    @Override
    public TipResursaDto toDto(TipResursa entity) {
        if (entity == null) return null;
        TipResursaDto dto = new TipResursaDto();
        dto.setIdTipResursa(entity.getIdTipaResursa());
        dto.setNaziv(entity.getNaziv());
        return dto;
    }
}
