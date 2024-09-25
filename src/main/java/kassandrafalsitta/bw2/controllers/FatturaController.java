package kassandrafalsitta.bw2.controllers;

import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.entities.Fattura;
import kassandrafalsitta.bw2.exceptions.BadRequestException;
import kassandrafalsitta.bw2.payloads.FatturaDTO;
import kassandrafalsitta.bw2.payloads.FatturaRespDTO;
import kassandrafalsitta.bw2.services.ClientiService;
import kassandrafalsitta.bw2.services.FattureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fatture")
public class FatturaController {
    @Autowired
    private FattureService fattureService;
    @Autowired
    private ClientiService clientiService;


    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Fattura> getAllFatture(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "id") String sortBy) {
        return this.fattureService.findAll(page, size, sortBy);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FatturaRespDTO createFattura(@RequestBody @Validated FatturaDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            return new FatturaRespDTO(this.fattureService.saveFattura(body).getId());
        }
    }


    @PutMapping("/{clienteId3}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Fattura findFatturaByIdAndUpdate(@PathVariable UUID clienteId, @RequestBody @Validated FatturaDTO body) {
        return fattureService.findByIdAndUpdate(clienteId, body);
    }

    @DeleteMapping("/{clienteId2}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findFatturaByIdAndDelete(@PathVariable UUID clienteId) {
        fattureService.findByIdAndDelete(clienteId);
    }

    // Endpoint per filtrare le fatture per cliente id - TESTATO
    @GetMapping("/{clienteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Fattura> getFattureByCliente(@PathVariable UUID clienteId) {
        Cliente cliente = clientiService.findById(clienteId);
        return fattureService.findByCliente(cliente);
    }

   @GetMapping("/filtro")
   @PreAuthorize("hasAuthority('ADMIN')")
   public List<Fattura> getFattureByFiltro(@RequestParam Map<String, String> queryParams) {
       if (queryParams.size() == 1) {
           if (queryParams.containsKey("stato")) {
               String stato = queryParams.get("stato");
               return fattureService.getFattureByStato(stato);
           } else if (queryParams.containsKey("data")) {
               LocalDate data = LocalDate.parse(queryParams.get("data"));
               return fattureService.getFattureByData(data);
           } else {
               throw new BadRequestException("Parametro non valido per la ricerca singola");
           }
       }
       // Se sono stati passati due parametri
       else if (queryParams.size() == 2) {
           if (queryParams.containsKey("min") && queryParams.containsKey("max")) {
               Double min = Double.valueOf(queryParams.get("min"));
               Double max = Double.valueOf(queryParams.get("max"));
               return fattureService.getFattureByImportoRange(min, max);
           } else {
               throw new BadRequestException("Coppia di parametri non valida");
           }
       }
       // Se ci sono più di due parametri, restituisce un errore
       else {
           throw new BadRequestException("Numero di parametri non valido");
       }


   }
}