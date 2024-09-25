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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

    @GetMapping("/{reservationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Fattura getFatturaById(@PathVariable UUID reservationId) {
        return fattureService.findById(reservationId);
    }

    @PutMapping("/{reservationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Fattura findFatturaByIdAndUpdate(@PathVariable UUID reservationId, @RequestBody @Validated FatturaDTO body) {
        return fattureService.findByIdAndUpdate(reservationId, body);
    }

    @DeleteMapping("/{reservationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findFatturaByIdAndDelete(@PathVariable UUID reservationId) {
        fattureService.findByIdAndDelete(reservationId);
    }

    // Endpoint per filtrare le fatture per cliente
    @GetMapping("/filtro/cliente/{clienteId}")
    public List<Fattura> getFattureByCliente(@PathVariable UUID clienteId) {
        Cliente cliente = clientiService.findById(clienteId);
        return fattureService.findByCliente(cliente);
    }

   /*// Endpoint per filtrare le fatture per stato
    @GetMapping("/filtro/stato/{statoId}")
    public List<Fattura> getFattureByStato(@PathVariable Long statoId) {
        StatoFattura stato = statoFatturaService.getStatoById(statoId);
        return fattureService.getFattureByStato(stato);
    }

    */

    // Endpoint per filtrare le fatture per range di importo
    @GetMapping("/filtro/importo")
    public List<Fattura> getFattureByImportoRange(@RequestParam Double min,
                                                  @RequestParam Double max) {
        return fattureService.getFattureByImportoRange(min, max);
    }

    // Endpoint per ottenere una fattura tramite cliente ID
    @GetMapping("/{clienteId}")
    public List<Fattura> getFatturebyClienteId(@PathVariable Cliente id) {
        return fattureService.findByClienteId(id);
    }

    // Endpoint per ottenere un cliente per data
    @GetMapping("/filtro/data")
    public List<Fattura> getFattureByData(@RequestParam LocalDate data){
        return fattureService.getFattureByData(data);
    }


    /*// Endpoint per ottenere un cliente per anno
    @GetMapping("/filtro/anno")
    public List<Fattura> getFattureByAnno(@RequestParam Integer anno){
        return fattureService.getFattureByAnno(anno);
    }

     */


}