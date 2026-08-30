package rs.ac.bg.fon.fonsledje.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.fon.fonsledje.connection.HttpResponse;
import rs.ac.bg.fon.fonsledje.connection.Response;
import rs.ac.bg.fon.fonsledje.dto.TipResursaDto;
import rs.ac.bg.fon.fonsledje.service.TipResursaService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tipovi-resursa")
public class TipResursaController {

    private final TipResursaService tipResursaService;

    public TipResursaController(TipResursaService tipResursaService) {
        this.tipResursaService = tipResursaService;
    }

    @GetMapping
    public ResponseEntity<Response> getAll() {
        List<TipResursaDto> tipovi = tipResursaService.findAll();
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Uspešno pronađeni tipovi resursa.", Map.of("values", tipovi), HttpStatus.OK)
        );
    }
}
