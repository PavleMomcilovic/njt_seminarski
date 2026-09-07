package rs.ac.bg.fon.fonsledje.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.fonsledje.entity.Osoba;
import rs.ac.bg.fon.fonsledje.repository.OsobaRepository;

@Service
public class OsobaUserDetailsService implements UserDetailsService {

    private final OsobaRepository osobaRepository;

    public OsobaUserDetailsService(OsobaRepository osobaRepository) {
        this.osobaRepository = osobaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Osoba osoba = osobaRepository.findByEmail(email);
        if (osoba == null) {
            throw new UsernameNotFoundException("Не постоји налог са имејлом '" + email + "'.");
        }
        return new OsobaPrincipal(osoba);
    }
}
