package rs.ac.bg.fon.fonsledje.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.fonsledje.config.OsobaPrincipal;
import rs.ac.bg.fon.fonsledje.connection.HttpResponse;
import rs.ac.bg.fon.fonsledje.connection.Response;
import rs.ac.bg.fon.fonsledje.dto.VestDto;
import rs.ac.bg.fon.fonsledje.entity.VestId;
import rs.ac.bg.fon.fonsledje.service.VestService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vesti")
public class VestController {

    private final VestService vestService;

    public VestController(VestService vestService) {
        this.vestService = vestService;
    }

    @GetMapping
    public ResponseEntity<Response> getAll() {
        List<VestDto> vesti = vestService.findAll();
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Успешно пронађене вести.", Map.of("values", vesti), HttpStatus.OK)
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<Response> create(@RequestBody VestDto dto, @AuthenticationPrincipal OsobaPrincipal principal) {
        VestDto saved = vestService.create(dto, principal.getIdOsobe());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                HttpResponse.getResponseWithData("Успешно креирана вест.", Map.of("value", saved), HttpStatus.CREATED)
        );
    }

    @PutMapping("/{idProfesora}/{idVesti}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<Response> update(@PathVariable Long idProfesora, @PathVariable Long idVesti,
                                            @RequestBody VestDto dto, @AuthenticationPrincipal OsobaPrincipal principal) {
        VestDto updated = vestService.update(new VestId(idProfesora, idVesti), dto, principal.getIdOsobe());
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Успешно измењена вест.", Map.of("value", updated), HttpStatus.OK)
        );
    }

    @DeleteMapping("/{idProfesora}/{idVesti}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<Response> delete(@PathVariable Long idProfesora, @PathVariable Long idVesti) {
        vestService.delete(new VestId(idProfesora, idVesti));
        return ResponseEntity.ok(HttpResponse.getResponse("Успешно обрисана вест.", HttpStatus.OK));
    }
}
