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

    @NotBlank(message = "Ово поље је обавезно")
    private String naziv;

    @NotBlank(message = "Ово поље је обавезно")
    private String tekst;

    @NotNull(message = "Ово поље је обавезно")
    private Date datum;

    @NotNull(message = "Ово поље је обавезно")
    private Long idProfesora;
}
