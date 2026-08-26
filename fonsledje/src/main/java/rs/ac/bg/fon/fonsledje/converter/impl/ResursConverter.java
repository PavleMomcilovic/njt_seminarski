package rs.ac.bg.fon.fonsledje.converter.impl;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.fonsledje.converter.Converter;
import rs.ac.bg.fon.fonsledje.dto.ResursDto;
import rs.ac.bg.fon.fonsledje.entity.Projekat;
import rs.ac.bg.fon.fonsledje.entity.Resurs;
import rs.ac.bg.fon.fonsledje.entity.ResursId;
import rs.ac.bg.fon.fonsledje.entity.TipResursa;

@Component
public class ResursConverter implements Converter<ResursDto, Resurs> {

    @Override
    public Resurs toEntity(ResursDto dto) {
        if (dto == null) return null;
        Resurs r = new Resurs();
        r.setId(new ResursId(dto.getIdProjekta(), dto.getIdResursa()));
        r.setVelicina(dto.getVelicina());
        r.setNaziv(dto.getNaziv());
        r.setOpis(dto.getOpis());

        Projekat projekat = new Projekat();
        projekat.setIdProjekta(dto.getIdProjekta());
        r.setProjekat(projekat);

        TipResursa tipResursa = new TipResursa();
        tipResursa.setIdTipaResursa(dto.getIdTipaResursa());
        r.setTipResursa(tipResursa);

        return r;
    }

    @Override
    public ResursDto toDto(Resurs entity) {
        if (entity == null) return null;
        ResursDto dto = new ResursDto();
        dto.setIdResursa(entity.getId() != null ? entity.getId().getIdResursa() : null);
        dto.setIdProjekta(entity.getId() != null ? entity.getId().getIdProjekta() : null);
        dto.setVelicina(entity.getVelicina());
        dto.setNaziv(entity.getNaziv());
        dto.setOpis(entity.getOpis());
        dto.setIdTipaResursa(entity.getTipResursa() != null ? entity.getTipResursa().getIdTipaResursa() : null);
        return dto;
    }
}
