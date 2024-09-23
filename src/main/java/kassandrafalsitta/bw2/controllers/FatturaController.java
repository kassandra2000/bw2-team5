package kassandrafalsitta.bw2.controllers;

import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.entities.Fattura;
import kassandrafalsitta.bw2.exceptions.BadRequestException;
import kassandrafalsitta.bw2.payloads.FatturaDTO;
import kassandrafalsitta.bw2.payloads.FatturaRespDTO;
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

    @GetMapping("/ricerca")
    public List<Fattura> getFatture(
            @RequestParam(required = false) Cliente cliente,
            @RequestParam(required = false) String stato,
            @RequestParam(required = false) LocalDate data,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) LocalDate anno,
            @RequestParam(required = false) BigDecimal minimo,
            @RequestParam(required = false) BigDecimal massimo) {
        if (cliente != null) {
            return fattureService.findByCliente(cliente);
        } else if (stato != null && !stato.isEmpty()) {
            return fattureService.getFattureFiltraPerStato(stato);
        } else if (data != null) {
            return fattureService.getFattureFiltraPerData(data);
        } else if (anno != null) {
            return fattureService.getFattureFiltraPerAnno(startDate, endDate);
        } else if (minimo != null && massimo != null) {
            return fattureService.getFattureFiltraPerRangeImporti(minimo, massimo);
        } else {
            return fattureService.getFattureFiltraPerAnno(LocalDate.now().getYear());
        }
    }
}