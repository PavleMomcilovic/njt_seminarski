package rs.ac.bg.fon.fonsledje.converter.impl;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.fonsledje.converter.Converter;
import rs.ac.bg.fon.fonsledje.dto.VerifikacijaDto;
import rs.ac.bg.fon.fonsledje.entity.Predmet;
import rs.ac.bg.fon.fonsledje.entity.Profesor;
import rs.ac.bg.fon.fonsledje.entity.Student;
import rs.ac.bg.fon.fonsledje.entity.Verifikacija;
import rs.ac.bg.fon.fonsledje.entity.VerifikacijaId;

@Component
public class VerifikacijaConverter implements Converter<VerifikacijaDto, Verifikacija> {

    @Override
    public Verifikacija toEntity(VerifikacijaDto dto) {
        if (dto == null) return null;
        Verifikacija v = new Verifikacija();
        v.setId(new VerifikacijaId(dto.getIdStudenta(), dto.getIdPredmeta()));
        v.setStatus(dto.getStatus());
        v.setOcena(dto.getOcena());
        v.setDatum(dto.getDatum());

        Student student = new Student();
        student.setIdOsobe(dto.getIdStudenta());
        v.setStudent(student);

        Predmet predmet = new Predmet();
        predmet.setIdPredmeta(dto.getIdPredmeta());
        v.setPredmet(predmet);

        if (dto.getIdProfesora() != null) {
            Profesor profesor = new Profesor();
            profesor.setIdOsobe(dto.getIdProfesora());
            v.setProfesor(profesor);
        }

        return v;
    }

    @Override
    public VerifikacijaDto toDto(Verifikacija entity) {
        if (entity == null) return null;
        VerifikacijaDto dto = new VerifikacijaDto();
        dto.setIdStudenta(entity.getId() != null ? entity.getId().getIdStudenta() : null);
        dto.setIdPredmeta(entity.getId() != null ? entity.getId().getIdPredmeta() : null);
        dto.setStatus(entity.getStatus());
        dto.setOcena(entity.getOcena());
        dto.setDatum(entity.getDatum());
        dto.setIdProfesora(entity.getProfesor() != null ? entity.getProfesor().getIdOsobe() : null);
        return dto;
    }
}
