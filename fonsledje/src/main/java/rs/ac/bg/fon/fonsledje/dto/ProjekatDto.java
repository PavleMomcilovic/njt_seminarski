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

    @NotBlank(message = "Ово поље је обавезно")
    private String naziv;

    @NotBlank(message = "Ово поље је обавезно")
    private String opis;

    @NotNull(message = "Ово поље је обавезно")
    private Long idStudenta;

    @NotNull(message = "Ово поље је обавезно")
    private Long idPredmeta;
}
