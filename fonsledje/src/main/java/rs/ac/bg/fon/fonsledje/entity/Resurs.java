package rs.ac.bg.fon.fonsledje.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;

@Entity
@Table(name = "resurs")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resurs implements Serializable {
    @EmbeddedId
    private ResursId id;

    @NotNull(message = "Ово поље је обавезно")
    @Column(name = "velicina", nullable = false)
    private Long velicina;

    @NotBlank(message = "Ово поље је обавезно")
    @Column(name = "naziv", nullable = false)
    private String naziv;

    @NotBlank(message = "Ово поље је обавезно")
    @Column(name = "opis", nullable = false, columnDefinition = "TEXT")
    private String opis;

    @NotNull(message = "Ово поље је обавезно")
    @JdbcTypeCode(SqlTypes.VARBINARY)
    @Column(name = "sadrzaj", nullable = false)
    private byte[] sadrzaj;

    @MapsId("idProjekta")
    @ManyToOne
    @JoinColumn(name = "idProjekta", nullable = false)
    private Projekat projekat;

    @ManyToOne
    @JoinColumn(name = "idTipaResursa", nullable = false)
    private TipResursa tipResursa;
}
