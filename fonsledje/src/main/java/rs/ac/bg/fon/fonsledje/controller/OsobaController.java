package rs.ac.bg.fon.fonsledje.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.fonsledje.config.OsobaPrincipal;
import rs.ac.bg.fon.fonsledje.connection.HttpResponse;
import rs.ac.bg.fon.fonsledje.connection.Response;
import rs.ac.bg.fon.fonsledje.dto.OsobaDto;
import rs.ac.bg.fon.fonsledje.service.OsobaService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/osoba")
public class OsobaController {

    private final OsobaService osobaService;

    public OsobaController(OsobaService osobaService) {
        this.osobaService = osobaService;
    }

    @PostMapping("/registracija")
    public ResponseEntity<Response> register(@Valid @RequestBody OsobaDto dto) {
        OsobaDto saved = osobaService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                HttpResponse.getResponseWithData("Registracija uspešna.", Map.of("value", saved), HttpStatus.CREATED)
        );
    }

    @GetMapping
    public ResponseEntity<Response> getAll() {
        List<OsobaDto> osobe = osobaService.findAll();
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Uspešno pronađene osobe.", Map.of("values", osobe), HttpStatus.OK)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getById(@PathVariable Long id) {
        OsobaDto osoba = osobaService.findById(id);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Uspešno pronađena osoba.", Map.of("value", osoba), HttpStatus.OK)
        );
    }

    @GetMapping("/pretraga")
    public ResponseEntity<Response> search(@RequestParam(required = false) String ime,
                                            @RequestParam(required = false) String prezime) {
        List<OsobaDto> osobe = osobaService.search(ime, prezime);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Uspešno pronađene osobe.", Map.of("values", osobe), HttpStatus.OK)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateCredentials(@PathVariable Long id, @RequestBody OsobaDto dto,
                                                        @AuthenticationPrincipal OsobaPrincipal principal) {
        OsobaDto updated = osobaService.updateCredentials(id, dto, principal.getIdOsobe(), principal.isProfesor());
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Kredencijali uspešno izmenjeni.", Map.of("value", updated), HttpStatus.OK)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id, @AuthenticationPrincipal OsobaPrincipal principal) {
        osobaService.delete(id, principal.getIdOsobe(), principal.isProfesor());
        return ResponseEntity.ok(HttpResponse.getResponse("Nalog uspešno obrisan.", HttpStatus.OK));
    }
}
