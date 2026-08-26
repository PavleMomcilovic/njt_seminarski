package rs.ac.bg.fon.fonsledje.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifikacijaId implements Serializable {
    @Column(name = "idStudenta")
    private Long idStudenta;

    @Column(name = "idPredmeta")
    private Long idPredmeta;
}
