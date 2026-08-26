package rs.ac.bg.fon.fonsledje.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResursDto {
    private Long idResursa;

    @NotNull(message = "Ovo polje je obavezno")
    private Long velicina;

    @NotBlank(message = "Ovo polje je obavezno")
    private String naziv;

    @NotBlank(message = "Ovo polje je obavezno")
    private String opis;

    @NotNull(message = "Ovo polje je obavezno")
    private Long idProjekta;

    @NotNull(message = "Ovo polje je obavezno")
    private Long idTipaResursa;
}
