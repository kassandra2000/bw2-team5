package kassandrafalsitta.bw2.controllers;

import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.entities.Fattura;
import kassandrafalsitta.bw2.exceptions.BadRequestException;
import kassandrafalsitta.bw2.exceptions.NotFoundException;
import kassandrafalsitta.bw2.payloads.ClientiDTO;
import kassandrafalsitta.bw2.payloads.ClientiRuoloDTO;
import kassandrafalsitta.bw2.payloads.FatturaDTO;
import kassandrafalsitta.bw2.services.ClientiService;
import kassandrafalsitta.bw2.services.FattureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clienti")
public class ClientiController {
    @Autowired
    private ClientiService clientiService;
    @Autowired
    private FattureService fattureService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Cliente> getAllClienti(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "id") String sortBy) {
        return this.clientiService.findAll(page, size, sortBy);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Cliente getClienteById(@PathVariable UUID userId) {
        return clientiService.findById(userId);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Cliente findClienteByIdAndUpdate(@PathVariable UUID userId, @RequestBody @Validated ClientiDTO body) {
        return clientiService.findByIdAndUpdate(userId, body);
    }

    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Cliente findByIdAndUpdateRole(@PathVariable UUID userId, @RequestBody @Validated ClientiRuoloDTO body) {
        return clientiService.findByIdAndUpdateRuolo(userId, body);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findClienteByIdAndDelete(@PathVariable UUID userId) {
        clientiService.findByIdAndDelete(userId);
    }

    @PostMapping("/{authorId}/avatar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Cliente uploadCover(@PathVariable UUID userId, @RequestParam("avatar") MultipartFile image) throws IOException {
        return this.clientiService.uploadImage(userId, image);
    }

    // me
    @GetMapping("/me")
    public Cliente getProfile(@AuthenticationPrincipal Cliente currentAuthenticatedCliente) {
        return currentAuthenticatedCliente;
    }

    @PutMapping("/me")
    public Cliente updateProfile(@AuthenticationPrincipal Cliente currentAuthenticatedCliente, @RequestBody ClientiDTO body) {
        return this.clientiService.findByIdAndUpdate(currentAuthenticatedCliente.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Cliente currentAuthenticatedCliente) {
        this.clientiService.findByIdAndDelete(currentAuthenticatedCliente.getId());
    }

    @PostMapping("/me/avatar")
    public Cliente uploadMyCover(@AuthenticationPrincipal Cliente currentAuthenticatedCliente, @RequestParam("avatar") MultipartFile image) throws IOException {
        return this.clientiService.uploadImage(currentAuthenticatedCliente.getId(), image);
    }

    // me/fatture
    @GetMapping("/me/event")
    @PreAuthorize("hasAuthority('EVENT_ORGANIZER')")
    public List<Fattura> getProfileFattura(@AuthenticationPrincipal Cliente currentAuthenticatedCliente) {
        return this.fattureService.findByCliente(currentAuthenticatedCliente);
    }

    @GetMapping("/me/event/{clientId}")
    @PreAuthorize("hasAuthority('EVENT_ORGANIZER')")
    public Fattura getProfileFatturaById(@AuthenticationPrincipal Cliente currentAuthenticatedCliente, @PathVariable UUID clientId) {
        List<Fattura> eventList = this.fattureService.findByCliente(currentAuthenticatedCliente);
        Fattura userFattura = eventList.stream().filter(event -> event.getId().equals(clientId)).findFirst()
                .orElseThrow(() -> new NotFoundException(clientId));
        return fattureService.findById(userFattura.getId());
    }


    @PutMapping("/me/event/{clientId}")
    @PreAuthorize("hasAuthority('EVENT_ORGANIZER')")
    public Fattura updateProfileFattura(@AuthenticationPrincipal Cliente currentAuthenticatedCliente, @PathVariable UUID clientId, @RequestBody FatturaDTO body) {
        List<Fattura> eventList = this.fattureService.findByCliente(currentAuthenticatedCliente);
        Fattura userFattura = eventList.stream().filter(event -> event.getId().equals(clientId)).findFirst()
                .orElseThrow(() -> new NotFoundException(clientId));
        return this.fattureService.findByIdAndUpdate(userFattura.getId(), body);
    }

    @DeleteMapping("/me/event/{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('EVENT_ORGANIZER')")
    public void deleteProfileFattura(@AuthenticationPrincipal Cliente currentAuthenticatedCliente, @PathVariable UUID clientId) {
        List<Fattura> eventList = this.fattureService.findByCliente(currentAuthenticatedCliente);
        Fattura userFattura = eventList.stream().filter(event -> event.getId().equals(clientId)).findFirst()
                .orElseThrow(() -> new NotFoundException(clientId));
        this.fattureService.findByIdAndDelete(userFattura.getId());
    }

    // Endpoint per ottenere tutti i clienti
    @GetMapping("/cerca")
    public List<Cliente> getAllClienti() {
        return clientiService.getAllClienti();
    }

    // Endpoint per filtrare i clienti per fatturato annuale
    @GetMapping("/filtro/min/max")
    public List<Cliente> getClientiByFatturatoAnnualeRange(@RequestParam Long min,
                                                            @RequestParam Long max) {
        return clientiService.getClientiByFatturatoAnnualeRange(min, max);
    }

    // Endpoint per filtrare i clienti per data di inserimento
    @GetMapping("/filtro/startDate/endDate")
    public List<Cliente> getClientiByDataInserimentoRange(@RequestParam String startDate,
                                                           @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return clientiService.getClientiByDataInserimentoRange(start, end);
    }

    // Endpoint per filtrare i clienti per data ultimo contatto
    @GetMapping("/filtro/startDate/endDate")
    public List<Cliente> getClientiByDataUltimoContattoRange(@RequestParam String startDate,
                                                             @RequestParam String endDate){
        LocalDate start = null;
        try {
            start = LocalDate.parse(startDate);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + startDate + " inserire nel seguente formato: AAAA/MM/GG");
        }LocalDate end = null;
        try {
            end = LocalDate.parse(endDate);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + endDate + " inserire nel seguente formato: AAAA/MM/GG");
        }
        return clientiService.getClientiByDataUltimoContattoRange(start,end);
    }

    //Endpoint per ordinare i clienti secondo il fatturato annuale

    @GetMapping("/ordina/fatturatoAnnuale")
    public List<Cliente> getClientiByFatturatoAnnuale(@RequestParam int fatturatoAnnuale){
        return clientiService.getClientiByFatturatoAnnuale(fatturatoAnnuale);
    }

    //Endpoint per ordinare i clienti secondo data inserimento

    @GetMapping("/ordina/dataInserimento")
    public List<Cliente> getClientiByDataInserimento(@RequestParam String dataInserimento){
        LocalDate start = null;
        try {
            start = LocalDate.parse(dataInserimento);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + dataInserimento + " inserire nel seguente formato: AAAA/MM/GG");
        }
        return clientiService.getClientiByDataInserimento(start);
    }

    //Endpoint per ordinare i clienti secondo data ultimo contatto

    @GetMapping("/ordina/dataUltimoContatto")
    public List<Cliente> getClientiByDataUltimoContatto(@RequestParam String dataUltimoContatto){
        LocalDate start = null;
        try {
            start = LocalDate.parse(dataUltimoContatto);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + dataUltimoContatto + " inserire nel seguente formato: AAAA/MM/GG");
        }
        return clientiService.getClientiByDataUltimoContatto(start);
    }





}
