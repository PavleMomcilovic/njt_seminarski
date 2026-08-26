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
public class VestDto {
    private Long idVesti;

    @NotBlank(message = "Ovo polje je obavezno")
    private String naziv;

    @NotBlank(message = "Ovo polje je obavezno")
    private String tekst;

    @NotNull(message = "Ovo polje je obavezno")
    private Date datum;

    @NotNull(message = "Ovo polje je obavezno")
    private Long idProfesora;
}
