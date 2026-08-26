package rs.ac.bg.fon.fonsledje.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "osoba")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class Osoba implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOsobe")
    private Long idOsobe;

    @NotNull(message = "Ovo polje je obavezno")
    @Email
    @Pattern(regexp = "[a-z0-9.]+@(student\\.)?fon\\.bg\\.ac\\.rs$")
    @Size(min = 1, max = 150)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull(message = "Ovo polje je obavezno")
    @Size(min = 9, max = 255)
    @Column(name = "sifra", nullable = false)
    private String sifra;

    @NotNull(message = "Ovo polje je obavezno")
    @Size(min = 1, max = 100)
    @Column(name = "ime", nullable = false)
    private String ime;

    @NotNull(message = "Ovo polje je obavezno")
    @Size(min = 1, max = 100)
    @Column(name = "prezime", nullable = false)
    private String prezime;
}
