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
public class PredmetDto {
    private Long idPredmeta;

    @NotBlank(message = "Ovo polje je obavezno")
    private String naziv;

    @NotNull(message = "Ovo polje je obavezno")
    private Long godina;

    @NotNull(message = "Ovo polje je obavezno")
    private Long semestar;

    @NotNull(message = "Ovo polje je obavezno")
    private Long idProfesora;
}
