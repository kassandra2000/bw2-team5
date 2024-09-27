package kassandrafalsitta.bw2.controllers;

import kassandrafalsitta.bw2.entities.Provincia;
import kassandrafalsitta.bw2.payloads.ProvinciaDTO;
import kassandrafalsitta.bw2.services.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/province")
public class ProvinceController {

    @Autowired
    private ProvinciaService provinceService;

    // Recupera tutte le province
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Provincia> getAllProvince() {
        return provinceService.findAll();
    }

    // Recupera una provincia tramite ID
    @GetMapping("/{provinciaId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Provincia getProvinciaById(@PathVariable UUID provinciaId) {
        return provinceService.findById(provinciaId);
    }

    // Crea una nuova provincia
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Provincia createProvincia(@RequestBody @Validated ProvinciaDTO body) {
        return provinceService.saveProvincia(body);
    }

    // Aggiorna una provincia tramite ID
    @PutMapping("/{provinciaId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Provincia updateProvincia(@PathVariable UUID provinciaId, @RequestBody @Validated ProvinciaDTO body) {
        return provinceService.findByIdAndUpdate(provinciaId, body);
    }

    // Elimina una provincia tramite ID
    @DeleteMapping("/{provinciaId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProvincia(@PathVariable UUID provinciaId) {
        provinceService.findByIdAndDelete(provinciaId);
    }
}
