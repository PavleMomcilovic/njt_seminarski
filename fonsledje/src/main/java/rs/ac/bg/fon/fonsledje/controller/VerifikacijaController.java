package rs.ac.bg.fon.fonsledje.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.fonsledje.config.OsobaPrincipal;
import rs.ac.bg.fon.fonsledje.connection.HttpResponse;
import rs.ac.bg.fon.fonsledje.connection.Response;
import rs.ac.bg.fon.fonsledje.dto.PotvrdaPredmetaRequest;
import rs.ac.bg.fon.fonsledje.dto.VerifikacijaDto;
import rs.ac.bg.fon.fonsledje.service.VerifikacijaService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/verifikacije")
public class VerifikacijaController {

    private final VerifikacijaService verifikacijaService;

    public VerifikacijaController(VerifikacijaService verifikacijaService) {
        this.verifikacijaService = verifikacijaService;
    }

    @GetMapping
    public ResponseEntity<Response> getAll() {
        List<VerifikacijaDto> verifikacije = verifikacijaService.findAll();
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Успешно пронађене верификације.", Map.of("values", verifikacije), HttpStatus.OK)
        );
    }

    @GetMapping("/student/{idStudenta}")
    public ResponseEntity<Response> getByStudent(@PathVariable Long idStudenta) {
        List<VerifikacijaDto> verifikacije = verifikacijaService.findByStudent(idStudenta);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Успешно пронађене верификације.", Map.of("values", verifikacije), HttpStatus.OK)
        );
    }

    @PostMapping("/potvrdi")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Response> potvrdiPredmete(@RequestBody PotvrdaPredmetaRequest request,
                                                      @AuthenticationPrincipal OsobaPrincipal principal) {
        List<VerifikacijaDto> kreirane = verifikacijaService.potvrdiPredmete(principal.getIdOsobe(), request.getIdPredmeta());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                HttpResponse.getResponseWithData("Успешно пријављени предмети.", Map.of("values", kreirane), HttpStatus.CREATED)
        );
    }

    @PutMapping("/{idStudenta}/{idPredmeta}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<Response> upisiOcenu(@PathVariable Long idStudenta, @PathVariable Long idPredmeta,
                                                @RequestBody VerifikacijaDto dto,
                                                @AuthenticationPrincipal OsobaPrincipal principal) {
        VerifikacijaDto updated = verifikacijaService.upisiOcenu(idStudenta, idPredmeta, dto, principal.getIdOsobe());
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Успешно уписана верификација.", Map.of("value", updated), HttpStatus.OK)
        );
    }

    @PostMapping("/{idStudenta}/{idPredmeta}/verifikuj")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<Response> verifikujStudenta(@PathVariable Long idStudenta, @PathVariable Long idPredmeta,
                                                        @AuthenticationPrincipal OsobaPrincipal principal) {
        VerifikacijaDto updated = verifikacijaService.verifikujStudenta(idStudenta, idPredmeta, principal.getIdOsobe());
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Студент успешно верификован за предмет.", Map.of("value", updated), HttpStatus.OK)
        );
    }
}
