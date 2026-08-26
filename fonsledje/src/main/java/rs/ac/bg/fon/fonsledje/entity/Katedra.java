package rs.ac.bg.fon.fonsledje.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "katedra")
@Getter
@Setter
@ToString(exclude = "profesori")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Katedra implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idKatedre", nullable = false, unique = true)
    private Long idKatedre;

    @NotBlank(message = "Ovo polje je obavezno")
    @Column(name = "naziv", nullable = false)
    private String naziv;

    @OneToMany(mappedBy = "katedra")
    private List<Profesor> profesori;
}
