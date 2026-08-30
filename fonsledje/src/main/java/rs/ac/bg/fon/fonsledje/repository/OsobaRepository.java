package rs.ac.bg.fon.fonsledje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.fonsledje.entity.Osoba;

import java.util.List;

public interface OsobaRepository extends JpaRepository<Osoba, Long> {
    Osoba findByEmail(String email);

    List<Osoba> findByImeContainingIgnoreCaseAndPrezimeContainingIgnoreCase(String ime, String prezime);
}
