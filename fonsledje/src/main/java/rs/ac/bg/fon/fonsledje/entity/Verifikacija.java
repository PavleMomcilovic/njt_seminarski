package rs.ac.bg.fon.fonsledje.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Ovo polje je obavezno")
    @Column(name = "status", nullable = false)
    private String status;

    @NotNull(message = "Ovo polje je obavezno")
    @Column(name = "ocena", nullable = false)
    private Long ocena;

    @NotNull(message = "Ovo polje je obavezno")
    @Temporal(TemporalType.DATE)
    @Column(name = "datum", nullable = false)
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
