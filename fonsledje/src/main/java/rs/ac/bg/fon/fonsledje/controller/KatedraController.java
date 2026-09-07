package rs.ac.bg.fon.fonsledje.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.fon.fonsledje.connection.HttpResponse;
import rs.ac.bg.fon.fonsledje.connection.Response;
import rs.ac.bg.fon.fonsledje.dto.KatedraDto;
import rs.ac.bg.fon.fonsledje.service.KatedraService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/katedre")
public class KatedraController {

    private final KatedraService katedraService;

    public KatedraController(KatedraService katedraService) {
        this.katedraService = katedraService;
    }

    @GetMapping
    public ResponseEntity<Response> getAll() {
        List<KatedraDto> katedre = katedraService.findAll();
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Успешно пронађене катедре.", Map.of("values", katedre), HttpStatus.OK)
        );
    }
}
