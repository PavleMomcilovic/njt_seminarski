package rs.ac.bg.fon.fonsledje.converter.impl;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.fonsledje.converter.Converter;
import rs.ac.bg.fon.fonsledje.dto.KatedraDto;
import rs.ac.bg.fon.fonsledje.entity.Katedra;

@Component
public class KatedraConverter implements Converter<KatedraDto, Katedra> {

    @Override
    public Katedra toEntity(KatedraDto dto) {
        if (dto == null) return null;
        Katedra k = new Katedra();
        k.setIdKatedre(dto.getIdKatedre());
        k.setNaziv(dto.getNaziv());
        return k;
    }

    @Override
    public KatedraDto toDto(Katedra entity) {
        if (entity == null) return null;
        return KatedraDto.builder()
                .idKatedre(entity.getIdKatedre())
                .naziv(entity.getNaziv())
                .build();
    }
}
