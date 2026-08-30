package rs.ac.bg.fon.fonsledje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.bg.fon.fonsledje.entity.Resurs;
import rs.ac.bg.fon.fonsledje.entity.ResursId;

import java.util.List;

public interface ResursRepository extends JpaRepository<Resurs, ResursId> {
    List<Resurs> findByProjekat_IdProjekta(Long idProjekta);

    @Query("select max(r.id.idResursa) from Resurs r where r.projekat.idProjekta = :idProjekta")
    Long findMaxIdResursaByProjekat(@Param("idProjekta") Long idProjekta);
}
