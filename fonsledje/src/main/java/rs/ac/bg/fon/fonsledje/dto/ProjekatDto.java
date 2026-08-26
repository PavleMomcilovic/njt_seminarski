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
public class ProjekatDto {
    private Long idProjekta;

    @NotBlank(message = "Ovo polje je obavezno")
    private String naziv;

    @NotBlank(message = "Ovo polje je obavezno")
    private String opis;

    @NotNull(message = "Ovo polje je obavezno")
    private Long idStudenta;
}
