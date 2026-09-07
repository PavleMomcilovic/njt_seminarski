package rs.ac.bg.fon.fonsledje.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.fon.fonsledje.config.OsobaPrincipal;
import rs.ac.bg.fon.fonsledje.connection.HttpResponse;
import rs.ac.bg.fon.fonsledje.connection.Response;
import rs.ac.bg.fon.fonsledje.dto.ResursDto;
import rs.ac.bg.fon.fonsledje.entity.Resurs;
import rs.ac.bg.fon.fonsledje.entity.ResursId;
import rs.ac.bg.fon.fonsledje.service.ResursService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/resursi")
public class ResursController {

    private final ResursService resursService;

    public ResursController(ResursService resursService) {
        this.resursService = resursService;
    }

    @GetMapping
    public ResponseEntity<Response> getAll() {
        List<ResursDto> resursi = resursService.getAllResurs();
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Успешно пронађени ресурси.", Map.of("values", resursi), HttpStatus.OK)
        );
    }

    @GetMapping("/projekat/{idProjekta}")
    public ResponseEntity<Response> getByProjekat(@PathVariable Long idProjekta) {
        List<ResursDto> resursi = resursService.getByProjekat(idProjekta);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Успешно пронађени ресурси.", Map.of("values", resursi), HttpStatus.OK)
        );
    }

    @GetMapping("/{idProjekta}/{idResursa}")
    public ResponseEntity<Response> getById(@PathVariable Long idProjekta, @PathVariable Long idResursa) {
        ResursDto resurs = resursService.getResurs(new ResursId(idProjekta, idResursa));
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Успешно пронађен ресурс.", Map.of("value", resurs), HttpStatus.OK)
        );
    }

    @GetMapping("/{idProjekta}/{idResursa}/preuzmi")
    public void preuzmi(@PathVariable Long idProjekta, @PathVariable Long idResursa, HttpServletResponse response) throws IOException {
        Resurs resurs = resursService.preuzmi(new ResursId(idProjekta, idResursa));
        response.setContentType(pogodiContentType(resurs));
        response.setHeader("Content-Disposition", "attachment; filename=\"" + resurs.getNaziv() + "\"");
        response.getOutputStream().write(resurs.getSadrzaj());
        response.getOutputStream().flush();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Response> create(@RequestPart("resurs") ResursDto dto, @RequestPart("file") MultipartFile file,
                                            @AuthenticationPrincipal OsobaPrincipal principal) {
        ResursDto saved = resursService.addResurs(dto, file, principal.getIdOsobe());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                HttpResponse.getResponseWithData("Успешно додат ресурс.", Map.of("value", saved), HttpStatus.CREATED)
        );
    }

    @PutMapping(value = "/{idProjekta}/{idResursa}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> update(@PathVariable Long idProjekta, @PathVariable Long idResursa,
                                            @RequestPart("resurs") ResursDto dto,
                                            @RequestPart(value = "file", required = false) MultipartFile file,
                                            @AuthenticationPrincipal OsobaPrincipal principal) {
        ResursDto updated = resursService.updateResurs(new ResursId(idProjekta, idResursa), dto, file,
                principal.getIdOsobe(), principal.isProfesor());
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Успешно измењен ресурс.", Map.of("value", updated), HttpStatus.OK)
        );
    }

    @DeleteMapping("/{idProjekta}/{idResursa}")
    public ResponseEntity<Response> delete(@PathVariable Long idProjekta, @PathVariable Long idResursa,
                                            @AuthenticationPrincipal OsobaPrincipal principal) {
        String poruka = resursService.deleteResurs(new ResursId(idProjekta, idResursa), principal.getIdOsobe(), principal.isProfesor());
        return ResponseEntity.ok(HttpResponse.getResponse(poruka, HttpStatus.OK));
    }

    private String pogodiContentType(Resurs resurs) {
        String naziv = resurs.getNaziv() == null ? "" : resurs.getNaziv().toLowerCase(Locale.ROOT);
        if (naziv.endsWith(".png")) return MediaType.IMAGE_PNG_VALUE;
        if (naziv.endsWith(".jpg") || naziv.endsWith(".jpeg")) return MediaType.IMAGE_JPEG_VALUE;
        if (naziv.endsWith(".gif")) return MediaType.IMAGE_GIF_VALUE;
        if (naziv.endsWith(".txt")) return MediaType.TEXT_PLAIN_VALUE;
        if (naziv.endsWith(".zip")) return "application/zip";

        String tip = resurs.getTipResursa() != null && resurs.getTipResursa().getNaziv() != null
                ? resurs.getTipResursa().getNaziv().toLowerCase(Locale.ROOT) : "";
        if (tip.contains("zip")) return "application/zip";
        if (tip.contains("tekst")) return MediaType.TEXT_PLAIN_VALUE;
        if (tip.contains("slik")) return MediaType.IMAGE_JPEG_VALUE;
        return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }
}
