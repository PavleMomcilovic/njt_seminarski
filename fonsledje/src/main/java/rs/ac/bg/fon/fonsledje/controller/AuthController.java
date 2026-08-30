package rs.ac.bg.fon.fonsledje.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.fonsledje.config.OsobaPrincipal;
import rs.ac.bg.fon.fonsledje.connection.HttpResponse;
import rs.ac.bg.fon.fonsledje.connection.Response;
import rs.ac.bg.fon.fonsledje.dto.LoginRequest;
import rs.ac.bg.fon.fonsledje.dto.OsobaDto;
import rs.ac.bg.fon.fonsledje.service.OsobaService;

import java.util.Map;

@RestController
@RequestMapping("/api/osoba")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final OsobaService osobaService;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public AuthController(AuthenticationManager authenticationManager, OsobaService osobaService) {
        this.authenticationManager = authenticationManager;
        this.osobaService = osobaService;
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest,
                                           HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSifra()));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, response);

            OsobaDto osoba = osobaService.findByEmail(loginRequest.getEmail());
            return ResponseEntity.ok(
                    HttpResponse.getResponseWithData("Uspešna prijava.", Map.of("value", osoba), HttpStatus.OK)
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    HttpResponse.getResponse("Pogrešan email ili lozinka.", HttpStatus.UNAUTHORIZED)
            );
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(HttpResponse.getResponse("Uspešna odjava.", HttpStatus.OK));
    }

    @GetMapping("/me")
    public ResponseEntity<Response> me(@AuthenticationPrincipal OsobaPrincipal principal) {
        OsobaDto osoba = osobaService.findById(principal.getIdOsobe());
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData("Trenutno ulogovana osoba.", Map.of("value", osoba), HttpStatus.OK)
        );
    }
}
