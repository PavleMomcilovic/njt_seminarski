package rs.ac.bg.fon.fonsledje.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.fon.fonsledje.connection.HttpResponse;
import rs.ac.bg.fon.fonsledje.connection.Response;
import rs.ac.bg.fon.fonsledje.dto.ZvanjeDto;
import rs.ac.bg.fon.fonsledje.service.ZvanjeService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/zvanja")
public class ZvanjeController {

    private final ZvanjeService zvanjeService;

    public ZvanjeController(ZvanjeService zvanjeService) {
        this.zvanjeService = zvanjeService;
    }

    @GetMapping
    public ResponseEntity<Response> getAll() {
        List<ZvanjeDto> zvanja = zvanjeService.findAll();
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Uspešno pronađena zvanja.", Map.of("values", zvanja), HttpStatus.OK)
        );
    }
}
