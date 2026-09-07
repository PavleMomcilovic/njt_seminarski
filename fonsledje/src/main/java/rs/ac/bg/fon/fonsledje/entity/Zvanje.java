package rs.ac.bg.fon.fonsledje.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "zvanje")
@Getter
@Setter
@ToString(exclude = "profesori")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zvanje implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idZvanja", nullable = false, unique = true)
    private Long idZvanja;

    @NotBlank(message = "Ово поље је обавезно")
    @Column(name = "naziv", nullable = false)
    private String naziv;

    @OneToMany(mappedBy = "zvanje")
    private List<Profesor> profesori;
}
