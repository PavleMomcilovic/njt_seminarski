package rs.ac.bg.fon.fonsledje.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "tipResursa")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipResursa implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTipaResursa", nullable = false, unique = true)
    private Long idTipaResursa;

    @Column(name = "naziv", nullable = false)
    @NotBlank(message = "Ovo polje je obavezno")
    private String naziv;
}
