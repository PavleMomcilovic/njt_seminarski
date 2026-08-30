package rs.ac.bg.fon.fonsledje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.bg.fon.fonsledje.entity.Vest;
import rs.ac.bg.fon.fonsledje.entity.VestId;

public interface VestRepository extends JpaRepository<Vest, VestId> {
    @Query("select max(v.id.idVesti) from Vest v where v.id.idProfesora = :idProfesora")
    Long findMaxIdVestiByProfesor(@Param("idProfesora") Long idProfesora);
}
