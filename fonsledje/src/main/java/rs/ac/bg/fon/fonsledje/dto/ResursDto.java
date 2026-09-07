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

    @NotNull(message = "Ово поље је обавезно")
    private Long velicina;

    @NotBlank(message = "Ово поље је обавезно")
    private String naziv;

    @NotBlank(message = "Ово поље је обавезно")
    private String opis;

    @NotNull(message = "Ово поље је обавезно")
    private Long idProjekta;

    @NotNull(message = "Ово поље је обавезно")
    private Long idTipaResursa;
}
