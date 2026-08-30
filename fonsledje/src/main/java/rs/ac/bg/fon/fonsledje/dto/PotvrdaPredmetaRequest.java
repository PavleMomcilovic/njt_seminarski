package rs.ac.bg.fon.fonsledje.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PotvrdaPredmetaRequest {
    private List<Long> idPredmeta;
}
