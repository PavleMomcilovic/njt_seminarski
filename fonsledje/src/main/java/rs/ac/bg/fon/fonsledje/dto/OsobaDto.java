package rs.ac.bg.fon.fonsledje.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import rs.ac.bg.fon.fonsledje.entity.Status;

@Getter
@Setter
@ToString(exclude = "sifra")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OsobaDto {
    private Long idOsobe;

    @NotBlank(message = "Ovo polje je obavezno")
    @Email
    private String email;

    private String sifra;

    @NotBlank(message = "Ovo polje je obavezno")
    private String ime;

    @NotBlank(message = "Ovo polje je obavezno")
    private String prezime;

    @NotNull(message = "Ovo polje je obavezno")
    private TipOsobe tip;

    // Student
    private String brojIndeksa;
    private Status status;

    // Profesor
    private Long idKatedre;
    private Long idZvanja;
}
