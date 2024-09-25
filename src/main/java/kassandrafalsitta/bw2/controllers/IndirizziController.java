package kassandrafalsitta.bw2.controllers;

import kassandrafalsitta.bw2.entities.Indirizzo;
import kassandrafalsitta.bw2.payloads.IndirizzoDTO;
import kassandrafalsitta.bw2.services.IndirizziService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/indirizzi")
public class IndirizziController {

    @Autowired
    private IndirizziService indirizziService;

    // Recupera tutti gli indirizzi con supporto alla paginazione
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Indirizzo> getAllIndirizzi() {
        return indirizziService.findAll();
    }

    // Recupera un indirizzo tramite ID
    @GetMapping("/{indirizzoId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Indirizzo getIndirizzoById(@PathVariable UUID indirizzoId) {
        return indirizziService.findById(indirizzoId);
    }

    // Crea un nuovo indirizzo
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Indirizzo createIndirizzo(@RequestBody @Validated IndirizzoDTO body) {
        return indirizziService.saveIndirizzo(body);
    }
    // Aggiorna un indirizzo tramite ID
    @PutMapping("/{indirizzoId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Indirizzo updateIndirizzo(@PathVariable UUID indirizzoId, @RequestBody @Validated IndirizzoDTO body) {
        return indirizziService.findByIdAndUpdate(indirizzoId, body);
    }

    // Elimina un indirizzo tramite ID
    @DeleteMapping("/{indirizzoId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIndirizzo(@PathVariable UUID indirizzoId) {
        indirizziService.findByIdAndDelete(indirizzoId);
    }
}
