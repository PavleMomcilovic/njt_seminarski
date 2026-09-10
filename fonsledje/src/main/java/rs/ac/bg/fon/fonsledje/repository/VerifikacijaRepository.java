package rs.ac.bg.fon.fonsledje.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.fonsledje.entity.Verifikacija;
import rs.ac.bg.fon.fonsledje.entity.VerifikacijaId;

import java.util.List;

public interface VerifikacijaRepository extends JpaRepository<Verifikacija, VerifikacijaId> {
    Page<Verifikacija> findByStudent_IdOsobe(Long idStudenta, Pageable pageable);

    void deleteByStudent_IdOsobe(Long idStudenta);
}
