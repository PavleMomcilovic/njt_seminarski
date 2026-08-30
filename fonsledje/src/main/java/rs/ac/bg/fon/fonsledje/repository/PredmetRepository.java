package rs.ac.bg.fon.fonsledje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.fonsledje.entity.Predmet;

import java.util.List;

public interface PredmetRepository extends JpaRepository<Predmet, Long> {
    List<Predmet> findByNazivContainingIgnoreCase(String naziv);
}
