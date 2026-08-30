package rs.ac.bg.fon.fonsledje.dto;

import lombok.*;

@Getter
@Setter
@ToString(exclude = "sifra")
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String sifra;
}
