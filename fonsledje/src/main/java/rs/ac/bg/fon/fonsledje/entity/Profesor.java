package rs.ac.bg.fon.fonsledje.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "profesor")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Profesor extends Osoba {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "idKatedre", nullable = false)
    private Katedra katedra;

    @ManyToOne
    @JoinColumn(name = "idZvanja", nullable = false)
    private Zvanje zvanje;
}
