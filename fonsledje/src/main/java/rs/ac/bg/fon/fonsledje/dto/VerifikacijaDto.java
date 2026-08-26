package rs.ac.bg.fon.fonsledje.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Ovo polje je obavezno")
    private String status;

    @NotNull(message = "Ovo polje je obavezno")
    private Long ocena;

    @NotNull(message = "Ovo polje je obavezno")
    private Date datum;

    @NotNull(message = "Ovo polje je obavezno")
    private Long idStudenta;

    @NotNull(message = "Ovo polje je obavezno")
    private Long idPredmeta;

    @NotNull(message = "Ovo polje je obavezno")
    private Long idProfesora;
}
