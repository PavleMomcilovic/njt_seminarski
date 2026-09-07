package rs.ac.bg.fon.fonsledje.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifikacijaDto {
    @NotNull(message = "Ово поље је обавезно")
    private Boolean status;

    private Long ocena;

    private Date datum;

    @NotNull(message = "Ово поље је обавезно")
    private Long idStudenta;

    @NotNull(message = "Ово поље је обавезно")
    private Long idPredmeta;

    private Long idProfesora;
}
