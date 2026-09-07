package rs.ac.bg.fon.fonsledje.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredmetDto {
    private Long idPredmeta;

    @NotBlank(message = "Ово поље је обавезно")
    private String naziv;

    @NotNull(message = "Ово поље је обавезно")
    private Long godina;

    @NotNull(message = "Ово поље је обавезно")
    private Long semestar;

    private List<Long> idProfesori;
}
