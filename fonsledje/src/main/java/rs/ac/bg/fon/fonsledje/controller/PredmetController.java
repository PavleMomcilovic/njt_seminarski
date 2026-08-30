package rs.ac.bg.fon.fonsledje.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.fonsledje.config.OsobaPrincipal;
import rs.ac.bg.fon.fonsledje.connection.HttpResponse;
import rs.ac.bg.fon.fonsledje.connection.Response;
import rs.ac.bg.fon.fonsledje.dto.PredmetDto;
import rs.ac.bg.fon.fonsledje.service.PredmetService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/predmeti")
public class PredmetController {

    private final PredmetService predmetService;

    public PredmetController(PredmetService predmetService) {
        this.predmetService = predmetService;
    }

    @GetMapping
    public ResponseEntity<Response> getAll() {
        List<PredmetDto> predmeti = predmetService.findAll();
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Uspešno pronađeni predmeti.", Map.of("values", predmeti), HttpStatus.OK)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getById(@PathVariable Long id) {
        PredmetDto predmet = predmetService.findById(id);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Uspešno pronađen predmet.", Map.of("value", predmet), HttpStatus.OK)
        );
    }

    @GetMapping("/pretraga")
    public ResponseEntity<Response> search(@RequestParam(required = false) String naziv) {
        List<PredmetDto> predmeti = predmetService.search(naziv);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Uspešno pronađeni predmeti.", Map.of("values", predmeti), HttpStatus.OK)
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<Response> create(@RequestBody PredmetDto dto, @AuthenticationPrincipal OsobaPrincipal principal) {
        PredmetDto saved = predmetService.create(dto, principal.getIdOsobe());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                HttpResponse.getResponseWithData("Uspešno kreiran predmet.", Map.of("value", saved), HttpStatus.CREATED)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody PredmetDto dto) {
        PredmetDto updated = predmetService.update(id, dto);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Uspešno izmenjen predmet.", Map.of("value", updated), HttpStatus.OK)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        predmetService.delete(id);
        return ResponseEntity.ok(HttpResponse.getResponse("Uspešno obrisan predmet.", HttpStatus.OK));
    }
}
