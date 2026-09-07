package rs.ac.bg.fon.fonsledje.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipResursaDto {
    private Long idTipResursa;

    @NotBlank(message = "Ово поље је обавезно")
    private String naziv;
}
