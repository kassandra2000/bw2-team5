package kassandrafalsitta.bw2.controllers;

import kassandrafalsitta.bw2.entities.Utente;
import kassandrafalsitta.bw2.payloads.ClientiRuoloDTO;
import kassandrafalsitta.bw2.payloads.UtenteDTO;
import kassandrafalsitta.bw2.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/utenti")
public class UtenteController {
    @Autowired
    private UtenteService utenteService;


    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Utente> getAllUtenti(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "id") String sortBy) {
        return this.utenteService.findAll(page, size, sortBy);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Utente getUtenteById(@PathVariable UUID userId) {
        return utenteService.findByIdUser(userId);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Utente findUtenteByIdAndUpdate(@PathVariable UUID userId, @RequestBody @Validated UtenteDTO body) {
        return utenteService.findByIdAndUpdate(userId, body);
    }
    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Utente findByIdAndUpdateRole(@PathVariable UUID userId, @RequestBody @Validated ClientiRuoloDTO body) {
        return utenteService.findByIdAndUpdateRuolo(userId, body);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findUtenteByIdAndDelete(@PathVariable UUID userId) {
        utenteService.findByIdAndDelete(userId);
    }


    // me
    @GetMapping("/me")
    public Utente getProfile(@AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        return currentAuthenticatedUtente;
    }

    @PutMapping("/me")
    public Utente updateProfile(@AuthenticationPrincipal Utente currentAuthenticatedUtente, @RequestBody UtenteDTO body) {
        return this.utenteService.findByIdAndUpdate(currentAuthenticatedUtente.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        this.utenteService.findByIdAndDelete(currentAuthenticatedUtente.getId());
    }
}
