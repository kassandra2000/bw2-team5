package kassandrafalsitta.bw2.controllers;

import kassandrafalsitta.bw2.entities.Comune;
import kassandrafalsitta.bw2.payloads.ComuneDTO;
import kassandrafalsitta.bw2.services.ComuneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comuni")
public class ComuniController {

    @Autowired
    private ComuneService comuneService;

    // Recupera tutti i comuni
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Comune> getAllComuni() {
        return comuneService.findAll();
    }

    // Recupera un comune tramite ID
    @GetMapping("/{comuneId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Comune getComuneById(@PathVariable UUID comuneId) {
        return comuneService.findById(comuneId);
    }

    // Crea un nuovo comune
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Comune createComune(@RequestBody @Validated ComuneDTO body) {
        return comuneService.saveComune(body);
    }

    // Aggiorna un comune tramite ID
    @PutMapping("/{comuneId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Comune updateComune(@PathVariable UUID comuneId, @RequestBody @Validated ComuneDTO body) {
        return comuneService.findByIdAndUpdate(comuneId, body);
    }

    // Elimina un comune tramite ID
    @DeleteMapping("/{comuneId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComune(@PathVariable UUID comuneId) {
        comuneService.findByIdAndDelete(comuneId);
    }
}
