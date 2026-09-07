package rs.ac.bg.fon.fonsledje.converter.impl;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.fonsledje.converter.Converter;
import rs.ac.bg.fon.fonsledje.dto.PredmetDto;
import rs.ac.bg.fon.fonsledje.entity.Predmet;
import rs.ac.bg.fon.fonsledje.entity.Profesor;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PredmetConverter implements Converter<PredmetDto, Predmet> {

    @Override
    public Predmet toEntity(PredmetDto dto) {
        if (dto == null) return null;
        Predmet p = new Predmet();
        p.setIdPredmeta(dto.getIdPredmeta());
        p.setNaziv(dto.getNaziv());
        p.setGodina(dto.getGodina());
        p.setSemestar(dto.getSemestar());

        Profesor profesor = new Profesor();
        profesor.setIdOsobe(dto.getIdProfesora());
        p.setProfesorOdobrio(profesor);

        return p;
    }

    @Override
    public PredmetDto toDto(Predmet entity) {
        if (entity == null) return null;
        PredmetDto dto = new PredmetDto();
        dto.setIdPredmeta(entity.getIdPredmeta());
        dto.setNaziv(entity.getNaziv());
        dto.setGodina(entity.getGodina());
        dto.setSemestar(entity.getSemestar());
        dto.setIdProfesora(entity.getProfesorOdobrio() != null ? entity.getProfesorOdobrio().getIdOsobe() : null);
        dto.setIdProfesori(entity.getProfesori() == null ? List.of() :
                entity.getProfesori().stream().map(Profesor::getIdOsobe).collect(Collectors.toList()));
        return dto;
    }
}
