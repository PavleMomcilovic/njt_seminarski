package rs.ac.bg.fon.fonsledje.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "vest")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vest implements Serializable {
    @EmbeddedId
    private VestId id;

    @NotBlank(message = "Ovo polje je obavezno")
    @Column(name = "naziv", nullable = false)
    private String naziv;

    @NotBlank(message = "Ovo polje je obavezno")
    @Column(name = "tekst", nullable = false)
    private String tekst;

    @NotNull(message = "Ovo polje je obavezno")
    @Temporal(TemporalType.DATE)
    @Column(name = "datum", nullable = false)
    private Date datum;

    @MapsId("idProfesora")
    @ManyToOne
    @JoinColumn(name = "idProfesora", nullable = false)
    private Profesor profesor;
}
