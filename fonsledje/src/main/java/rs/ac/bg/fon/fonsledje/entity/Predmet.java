package rs.ac.bg.fon.fonsledje.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "predmet")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Predmet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPredmeta", nullable = false, unique = true)
    private Long idPredmeta;

    @NotBlank(message = "Ovo polje je obavezno")
    @Column(name = "naziv", nullable = false)
    private String naziv;

    @NotNull(message = "Ovo polje je obavezno")
    @Column(name = "godina", nullable = false)
    private Long godina;

    @NotNull(message = "Ovo polje je obavezno")
    @Column(name = "semestar", nullable = false)
    private Long semestar;

    @ManyToOne
    @JoinColumn(name = "idProfesora", nullable = false)
    private Profesor profesorOdobrio;
}
