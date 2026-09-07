package rs.ac.bg.fon.fonsledje.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.fonsledje.config.OsobaPrincipal;
import rs.ac.bg.fon.fonsledje.connection.HttpResponse;
import rs.ac.bg.fon.fonsledje.connection.Response;
import rs.ac.bg.fon.fonsledje.dto.ProjekatDto;
import rs.ac.bg.fon.fonsledje.service.ProjekatService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projekti")
public class ProjekatController {

    private final ProjekatService projekatService;

    public ProjekatController(ProjekatService projekatService) {
        this.projekatService = projekatService;
    }

    @GetMapping
    public ResponseEntity<Response> getAll() {
        List<ProjekatDto> projekti = projekatService.findAll();
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Uspešno pronađeni projekti.", Map.of("values", projekti), HttpStatus.OK)
        );
    }

    @GetMapping("/predmet/{idPredmeta}")
    public ResponseEntity<Response> getByPredmet(@PathVariable Long idPredmeta) {
        List<ProjekatDto> projekti = projekatService.findByPredmet(idPredmeta);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Uspešno pronađeni projekti.", Map.of("values", projekti), HttpStatus.OK)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getById(@PathVariable Long id) {
        ProjekatDto projekat = projekatService.findById(id);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Uspešno pronađen projekat.", Map.of("value", projekat), HttpStatus.OK)
        );
    }

    @GetMapping("/pretraga")
    public ResponseEntity<Response> search(@RequestParam(required = false) String naziv) {
        List<ProjekatDto> projekti = projekatService.search(naziv);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Uspešno pronađeni projekti.", Map.of("values", projekti), HttpStatus.OK)
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Response> create(@RequestBody ProjekatDto dto, @AuthenticationPrincipal OsobaPrincipal principal) {
        ProjekatDto saved = projekatService.create(dto, principal.getIdOsobe());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                HttpResponse.getResponseWithData("Uspešno kreiran projekat.", Map.of("value", saved), HttpStatus.CREATED)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody ProjekatDto dto,
                                            @AuthenticationPrincipal OsobaPrincipal principal) {
        ProjekatDto updated = projekatService.update(id, dto, principal.getIdOsobe(), principal.isProfesor());
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Uspešno izmenjen projekat.", Map.of("value", updated), HttpStatus.OK)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id, @AuthenticationPrincipal OsobaPrincipal principal) {
        projekatService.delete(id, principal.getIdOsobe(), principal.isProfesor());
        return ResponseEntity.ok(HttpResponse.getResponse("Uspešno obrisan projekat.", HttpStatus.OK));
    }
}
