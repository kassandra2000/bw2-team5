package kassandrafalsitta.bw2.controllers;

import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.entities.Fattura;
import kassandrafalsitta.bw2.exceptions.BadRequestException;
import kassandrafalsitta.bw2.exceptions.NotFoundException;
import kassandrafalsitta.bw2.payloads.ClientiRuoloDTO;
import kassandrafalsitta.bw2.payloads.ClientiUpdateDTO;
import kassandrafalsitta.bw2.payloads.FatturaDTO;
import kassandrafalsitta.bw2.payloads.IndirizziClientiDTO;
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
import java.util.List;
import java.util.Map;
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

    @GetMapping("/{userId:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Cliente getClienteById(@PathVariable UUID userId) {
        return clientiService.findById(userId);
    }

    @PutMapping("/{userId:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Cliente findClienteByIdAndUpdate(@PathVariable UUID userId, @RequestBody @Validated ClientiUpdateDTO body) {
        return clientiService.findByIdAndUpdate(userId, body);
    }

    @PatchMapping("/{userId:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Cliente findByIdAndUpdateRole(@PathVariable UUID userId, @RequestBody @Validated ClientiRuoloDTO body) {
        return clientiService.findByIdAndUpdateRuolo(userId, body);
    }

    @DeleteMapping("/{userId:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findClienteByIdAndDelete(@PathVariable UUID userId) {
        clientiService.findByIdAndDelete(userId);
    }

    @PostMapping("/{userId:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}/avatar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Cliente uploadCover(@PathVariable UUID userId, @RequestParam("avatar") MultipartFile image) throws IOException {
        return this.clientiService.uploadImage(userId, image);
    }


    @PutMapping("/{userId:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}/indirizzo")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Cliente findEventByIdAndUpdate(@PathVariable UUID userId, @RequestBody @Validated IndirizziClientiDTO body) {
        return this.clientiService.findByIdAndUpdateIndirizzi(userId, body);
    }

    // me
    @GetMapping("/me")
    public Cliente getProfile(@AuthenticationPrincipal Cliente currentAuthenticatedCliente) {
        return currentAuthenticatedCliente;
    }

    @PutMapping("/me")
    public Cliente updateProfile(@AuthenticationPrincipal Cliente currentAuthenticatedCliente, @RequestBody ClientiUpdateDTO body) {
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
    @GetMapping("/me/fattura")
    @PreAuthorize("hasAuthority('EVENT_ORGANIZER')")
    public List<Fattura> getProfileFattura(@AuthenticationPrincipal Cliente currentAuthenticatedCliente) {
        return this.fattureService.findByCliente(currentAuthenticatedCliente);
    }

    @GetMapping("/me/fattura/{clientId}")
    @PreAuthorize("hasAuthority('EVENT_ORGANIZER')")
    public Fattura getProfileFatturaById(@AuthenticationPrincipal Cliente currentAuthenticatedCliente, @PathVariable UUID clientId) {
        List<Fattura> fatturaList = this.fattureService.findByCliente(currentAuthenticatedCliente);
        Fattura userFattura = fatturaList.stream().filter(fattura -> fattura.getId().equals(clientId)).findFirst()
                .orElseThrow(() -> new NotFoundException(clientId));
        return fattureService.findById(userFattura.getId());
    }


    @PutMapping("/me/fattura/{clientId}")
    @PreAuthorize("hasAuthority('EVENT_ORGANIZER')")
    public Fattura updateProfileFattura(@AuthenticationPrincipal Cliente currentAuthenticatedCliente, @PathVariable UUID clientId, @RequestBody FatturaDTO body) {
        List<Fattura> fatturaList = this.fattureService.findByCliente(currentAuthenticatedCliente);
        Fattura userFattura = fatturaList.stream().filter(fattura -> fattura.getId().equals(clientId)).findFirst()
                .orElseThrow(() -> new NotFoundException(clientId));
        return this.fattureService.findByIdAndUpdate(userFattura.getId(), body);
    }

    @DeleteMapping("/me/fattura/{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('EVENT_ORGANIZER')")
    public void deleteProfileFattura(@AuthenticationPrincipal Cliente currentAuthenticatedCliente, @PathVariable UUID clientId) {
        List<Fattura> fatturaList = this.fattureService.findByCliente(currentAuthenticatedCliente);
        Fattura userFattura = fatturaList.stream().filter(fattura -> fattura.getId().equals(clientId)).findFirst()
                .orElseThrow(() -> new NotFoundException(clientId));
        this.fattureService.findByIdAndDelete(userFattura.getId());
    }

    // Endpoint per ottenere tutti i clienti
    @GetMapping("/cerca")
    public List<Cliente> getAllClienti() {
        return clientiService.getAllClienti();
    }

    @GetMapping("/filtroClienti")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Cliente> getClientiByFiltro(@RequestParam Map<String, String> queryParams) {
        // Verifica se c'Ã¨ solo un parametro
        if (queryParams.size() == 1) {
            if (queryParams.containsKey("fatturatoAnnuale")) {
                // Filtra per fatturato annuale
                int fatturatoAnnuale = Integer.parseInt(queryParams.get("fatturatoAnnuale"));
                return clientiService.getClientiByFatturatoAnnuale(fatturatoAnnuale);
            } else if (queryParams.containsKey("dataInserimento")) {
                // Filtra per data di inserimento (come stringa)
                String dataInserimento = queryParams.get("dataInserimento");
                return clientiService.getClientiByDataInserimento(dataInserimento);
            } else if (queryParams.containsKey("dataUltimoContatto")) {
                // Filtra per data ultimo contatto (come stringa)
                String dataUltimoContatto = queryParams.get("dataUltimoContatto");
                return clientiService.getClientiByDataUltimoContatto(dataUltimoContatto);
            } else if (queryParams.containsKey("nomeParziale")) {
                // Filtra per parte del nome
                String nomeParziale = queryParams.get("nomeParziale");
                return clientiService.getClientiByNomeParziale(nomeParziale);
            } else {
                throw new BadRequestException("Parametro non valido per la ricerca singola");
            }
        }
        // Verifica se ci sono due parametri
        else if (queryParams.size() == 2) {
            if (queryParams.containsKey("minFatturato") && queryParams.containsKey("maxFatturato")) {
                // Filtra per intervallo di fatturato
                Long minFatturato = Long.valueOf(queryParams.get("minFatturato"));
                Long maxFatturato = Long.valueOf(queryParams.get("maxFatturato"));
                return clientiService.getClientiByFatturatoAnnualeRange(minFatturato, maxFatturato);
            } else if (queryParams.containsKey("startDate") && queryParams.containsKey("endDate")) {
                // Filtra per intervallo di data di inserimento (come stringhe)
                String startDate = queryParams.get("startDate");
                String endDate = queryParams.get("endDate");
                return clientiService.getClientiByDataInserimentoRange(startDate, endDate);
            } else if (queryParams.containsKey("startUltimoContatto") && queryParams.containsKey("endUltimoContatto")) {
                // Filtra per intervallo di data ultimo contatto (come stringhe)
                String startUltimoContatto = queryParams.get("startUltimoContatto");
                String endUltimoContatto = queryParams.get("endUltimoContatto");
                return clientiService.getClientiByDataUltimoContattoRange(startUltimoContatto, endUltimoContatto);
            } else {
                throw new BadRequestException("Coppia di parametri non valida");
            }
        } else {
            throw new BadRequestException("Numero di parametri non valido");
        }
    }

    //ordina
    @GetMapping("/ordina")
    public List<Cliente> ordinaClienti(@RequestParam String criterio) {
        if ("nome".equalsIgnoreCase(criterio)) {
            return clientiService.getAllClientiByNomeAsc();
        } else if ("fatturato".equalsIgnoreCase(criterio)) {
            return clientiService.getAllClientiByFatturatoAnnualeAsc();
        } else if ("data-inserimento".equalsIgnoreCase(criterio)) {
            return clientiService.getAllClientiByDataInserimentoAsc();
        } else if ("data-ultimo-contatto".equalsIgnoreCase(criterio)) {
            return clientiService.getAllClientiByDataUltimoContattoAsc();
        } else if ("provincia".equalsIgnoreCase(criterio)) {
            return clientiService.getAllClientiBySedeLegaleProvinciaAsc();
        } else {
            throw new BadRequestException("Criterio di ordinamento non valido: " + criterio);
        }
    }
}


