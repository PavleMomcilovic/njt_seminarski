package rs.ac.bg.fon.fonsledje.entity;

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
    private Long idStudenta;

    private Long idPredmeta;
}
