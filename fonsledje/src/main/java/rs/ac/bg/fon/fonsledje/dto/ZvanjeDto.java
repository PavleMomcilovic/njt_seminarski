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

    @NotBlank(message = "Ово поље је обавезно")
    private String naziv;
}
