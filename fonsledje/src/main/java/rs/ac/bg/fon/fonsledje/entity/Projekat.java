package rs.ac.bg.fon.fonsledje.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "projekat")
@Getter
@Setter
@ToString(exclude = "resursi")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Projekat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProjekta", nullable = false, unique = true)
    private Long idProjekta;

    @NotBlank(message = "Ово поље је обавезно")
    @Column(name = "naziv", nullable = false)
    private String naziv;

    @NotBlank(message = "Ово поље је обавезно")
    @Column(name = "opis", nullable = false, columnDefinition = "TEXT")
    private String opis;

    @ManyToOne
    @JoinColumn(name = "idStudenta", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "idPredmeta", nullable = false)
    private Predmet predmet;

    @OneToMany(mappedBy = "projekat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resurs> resursi;
}
