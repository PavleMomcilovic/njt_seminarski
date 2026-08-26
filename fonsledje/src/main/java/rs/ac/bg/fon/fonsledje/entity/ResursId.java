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
public class ResursId implements Serializable {
    @Column(name = "idProjekta")
    private Long idProjekta;

    @Column(name = "idResursa")
    private Long idResursa;
}
