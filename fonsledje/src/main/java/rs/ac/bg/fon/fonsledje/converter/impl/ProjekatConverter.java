package rs.ac.bg.fon.fonsledje.converter.impl;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.fonsledje.converter.Converter;
import rs.ac.bg.fon.fonsledje.dto.ProjekatDto;
import rs.ac.bg.fon.fonsledje.entity.Projekat;
import rs.ac.bg.fon.fonsledje.entity.Student;

@Component
public class ProjekatConverter implements Converter<ProjekatDto, Projekat> {

    @Override
    public Projekat toEntity(ProjekatDto dto) {
        if (dto == null) return null;
        Projekat p = new Projekat();
        p.setIdProjekta(dto.getIdProjekta());
        p.setNaziv(dto.getNaziv());
        p.setOpis(dto.getOpis());

        Student student = new Student();
        student.setIdOsobe(dto.getIdStudenta());
        p.setStudent(student);

        return p;
    }

    @Override
    public ProjekatDto toDto(Projekat entity) {
        if (entity == null) return null;
        ProjekatDto dto = new ProjekatDto();
        dto.setIdProjekta(entity.getIdProjekta());
        dto.setNaziv(entity.getNaziv());
        dto.setOpis(entity.getOpis());
        dto.setIdStudenta(entity.getStudent() != null ? entity.getStudent().getIdOsobe() : null);
        return dto;
    }
}
