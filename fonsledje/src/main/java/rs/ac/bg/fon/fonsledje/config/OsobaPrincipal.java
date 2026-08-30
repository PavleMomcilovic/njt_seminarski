package rs.ac.bg.fon.fonsledje.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rs.ac.bg.fon.fonsledje.entity.Osoba;
import rs.ac.bg.fon.fonsledje.entity.Profesor;

import java.util.Collection;
import java.util.List;

public class OsobaPrincipal implements UserDetails {

    private final Long idOsobe;
    private final String email;
    private final String sifra;
    private final boolean profesor;

    public OsobaPrincipal(Osoba osoba) {
        this.idOsobe = osoba.getIdOsobe();
        this.email = osoba.getEmail();
        this.sifra = osoba.getSifra();
        this.profesor = osoba instanceof Profesor;
    }

    public Long getIdOsobe() {
        return idOsobe;
    }

    public boolean isProfesor() {
        return profesor;
    }

    public boolean isStudent() {
        return !profesor;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(profesor ? "ROLE_PROFESOR" : "ROLE_STUDENT"));
    }

    @Override
    public String getPassword() {
        return sifra;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
