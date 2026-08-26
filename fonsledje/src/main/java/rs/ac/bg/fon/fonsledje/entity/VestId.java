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
public class VestId implements Serializable {
    @Column(name = "idProfesora")
    private Long idProfesora;

    @Column(name = "idVesti")
    private Long idVesti;
}
