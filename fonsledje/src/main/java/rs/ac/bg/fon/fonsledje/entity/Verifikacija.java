package rs.ac.bg.fon.fonsledje.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "verifikacija")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Verifikacija implements Serializable {
    @EmbeddedId
    private VerifikacijaId id;

    @NotNull(message = "Ovo polje je obavezno")
    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "ocena")
    private Long ocena;

    @Temporal(TemporalType.DATE)
    @Column(name = "datum")
    private Date datum;

    @MapsId("idStudenta")
    @ManyToOne
    @JoinColumn(name = "idStudenta", nullable = false)
    private Student student;

    @MapsId("idPredmeta")
    @ManyToOne
    @JoinColumn(name = "idPredmeta", nullable = false)
    private Predmet predmet;

    @ManyToOne
    @JoinColumn(name = "idProfesora", nullable = false)
    private Profesor profesor;
}
