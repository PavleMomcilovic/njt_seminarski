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

    @NotBlank(message = "Ово поље је обавезно")
    @Email
    private String email;

    private String sifra;

    @NotBlank(message = "Ово поље је обавезно")
    private String ime;

    @NotBlank(message = "Ово поље је обавезно")
    private String prezime;

    @NotNull(message = "Ово поље је обавезно")
    private TipOsobe tip;

    // Student
    private String brojIndeksa;
    private Status status;

    // Profesor
    private Long idKatedre;
    private Long idZvanja;
}
