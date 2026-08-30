package rs.ac.bg.fon.fonsledje.converter.impl;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.fonsledje.converter.Converter;
import rs.ac.bg.fon.fonsledje.dto.ZvanjeDto;
import rs.ac.bg.fon.fonsledje.entity.Zvanje;

@Component
public class ZvanjeConverter implements Converter<ZvanjeDto, Zvanje> {

    @Override
    public Zvanje toEntity(ZvanjeDto dto) {
        if (dto == null) return null;
        Zvanje z = new Zvanje();
        z.setIdZvanja(dto.getIdZvanja());
        z.setNaziv(dto.getNaziv());
        return z;
    }

    @Override
    public ZvanjeDto toDto(Zvanje entity) {
        if (entity == null) return null;
        return ZvanjeDto.builder()
                .idZvanja(entity.getIdZvanja())
                .naziv(entity.getNaziv())
                .build();
    }
}
