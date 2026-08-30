package rs.ac.bg.fon.fonsledje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.fonsledje.entity.Projekat;

import java.util.List;

public interface ProjekatRepository extends JpaRepository<Projekat, Long> {
    List<Projekat> findByStudent_IdOsobe(Long idStudenta);

    List<Projekat> findByNazivContainingIgnoreCase(String naziv);
}
