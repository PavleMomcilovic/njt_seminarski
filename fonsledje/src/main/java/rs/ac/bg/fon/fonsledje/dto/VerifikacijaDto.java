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
    @NotNull(message = "Ovo polje je obavezno")
    private Boolean status;

    private Long ocena;

    private Date datum;

    @NotNull(message = "Ovo polje je obavezno")
    private Long idStudenta;

    @NotNull(message = "Ovo polje je obavezno")
    private Long idPredmeta;

    @NotNull(message = "Ovo polje je obavezno")
    private Long idProfesora;
}
