package rs.ac.bg.fon.fonsledje.converter.impl;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.fonsledje.converter.Converter;
import rs.ac.bg.fon.fonsledje.dto.VestDto;
import rs.ac.bg.fon.fonsledje.entity.Profesor;
import rs.ac.bg.fon.fonsledje.entity.Vest;
import rs.ac.bg.fon.fonsledje.entity.VestId;

@Component
public class VestConverter implements Converter<VestDto, Vest> {

    @Override
    public Vest toEntity(VestDto dto) {
        if (dto == null) return null;
        Vest v = new Vest();
        v.setId(new VestId(dto.getIdProfesora(), dto.getIdVesti()));
        v.setNaziv(dto.getNaziv());
        v.setTekst(dto.getTekst());
        v.setDatum(dto.getDatum());

        Profesor profesor = new Profesor();
        profesor.setIdOsobe(dto.getIdProfesora());
        v.setProfesor(profesor);

        return v;
    }

    @Override
    public VestDto toDto(Vest entity) {
        if (entity == null) return null;
        VestDto dto = new VestDto();
        dto.setIdVesti(entity.getId() != null ? entity.getId().getIdVesti() : null);
        dto.setIdProfesora(entity.getId() != null ? entity.getId().getIdProfesora() : null);
        dto.setNaziv(entity.getNaziv());
        dto.setTekst(entity.getTekst());
        dto.setDatum(entity.getDatum());
        return dto;
    }
}
