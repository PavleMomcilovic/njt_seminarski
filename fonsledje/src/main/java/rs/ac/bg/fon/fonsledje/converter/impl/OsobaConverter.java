package rs.ac.bg.fon.fonsledje.converter.impl;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.fonsledje.converter.Converter;
import rs.ac.bg.fon.fonsledje.dto.OsobaDto;
import rs.ac.bg.fon.fonsledje.dto.TipOsobe;
import rs.ac.bg.fon.fonsledje.entity.Katedra;
import rs.ac.bg.fon.fonsledje.entity.Osoba;
import rs.ac.bg.fon.fonsledje.entity.Profesor;
import rs.ac.bg.fon.fonsledje.entity.Student;
import rs.ac.bg.fon.fonsledje.entity.Zvanje;

@Component
public class OsobaConverter implements Converter<OsobaDto, Osoba> {

    @Override
    public Osoba toEntity(OsobaDto dto) {
        if (dto == null) return null;

        if (dto.getTip() == TipOsobe.STUDENT) {
            Student s = new Student();
            mapCommon(s, dto);
            s.setBrojIndeksa(dto.getBrojIndeksa());
            s.setStatus(dto.getStatus());
            return s;
        } else if (dto.getTip() == TipOsobe.PROFESOR) {
            Profesor p = new Profesor();
            mapCommon(p, dto);

            Katedra katedra = new Katedra();
            katedra.setIdKatedre(dto.getIdKatedre());
            p.setKatedra(katedra);

            Zvanje zvanje = new Zvanje();
            zvanje.setIdZvanja(dto.getIdZvanja());
            p.setZvanje(zvanje);
            return p;
        }
        throw new IllegalArgumentException("Nepoznat tip osobe: " + dto.getTip());
    }

    @Override
    public OsobaDto toDto(Osoba entity) {
        if (entity == null) return null;
        OsobaDto dto = new OsobaDto();
        mapCommon(dto, entity);

        if (entity instanceof Student s) {
            dto.setTip(TipOsobe.STUDENT);
            dto.setBrojIndeksa(s.getBrojIndeksa());
            dto.setStatus(s.getStatus());
        } else if (entity instanceof Profesor p) {
            dto.setTip(TipOsobe.PROFESOR);
            dto.setIdKatedre(p.getKatedra() != null ? p.getKatedra().getIdKatedre() : null);
            dto.setIdZvanja(p.getZvanje() != null ? p.getZvanje().getIdZvanja() : null);
        }
        return dto;
    }

    private void mapCommon(Osoba entity, OsobaDto dto) {
        entity.setIdOsobe(dto.getIdOsobe());
        entity.setEmail(dto.getEmail());
        entity.setSifra(dto.getSifra());
        entity.setIme(dto.getIme());
        entity.setPrezime(dto.getPrezime());
    }

    private void mapCommon(OsobaDto dto, Osoba entity) {
        dto.setIdOsobe(entity.getIdOsobe());
        dto.setEmail(entity.getEmail());
        dto.setSifra(null);
        dto.setIme(entity.getIme());
        dto.setPrezime(entity.getPrezime());
    }
}
