package rs.ac.bg.fon.fonsledje.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZvanjeDto {
    private Long idZvanja;

    @NotBlank(message = "Ovo polje je obavezno")
    private String naziv;
}
