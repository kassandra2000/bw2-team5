package kassandrafalsitta.bw2.services;

import jakarta.transaction.Transactional;
import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.entities.Fattura;
import kassandrafalsitta.bw2.exceptions.BadRequestException;
import kassandrafalsitta.bw2.exceptions.NotFoundException;
import kassandrafalsitta.bw2.payloads.FatturaDTO;
import kassandrafalsitta.bw2.repositories.ClientiRepository;
import kassandrafalsitta.bw2.repositories.FattureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FattureService {
    @Autowired
    private FattureRepository fattureRepository;
    @Autowired
    private ClientiService clientiService;
    @Autowired
    private ClientiRepository clientiRepository;

    public Page<Fattura> findAll(int page, int size, String sortBy) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.fattureRepository.findAll(pageable);
    }
    @Transactional
    public Fattura saveFattura(FatturaDTO body) {
        UUID clienteID;
        try {
            clienteID = UUID.fromString(body.clienteID());
        } catch (NumberFormatException e) {
            throw new BadRequestException("L'UUID del cliente non è corretto");
        }

        // Recupera il cliente
        Cliente cliente = clientiService.findById(clienteID);

        // Parsing e validazione della data
        LocalDate dataFattura;
        try {
            dataFattura = LocalDate.parse(body.dataFattura());
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + body.dataFattura() +
                    ". Inserire nel formato: AAAA/MM/GG");
        }

        // Crea una nuova istanza di Fattura
        Fattura fattura = new Fattura(
                dataFattura,
                body.importo(),
                body.numero(),
                body.statoFattura(),
                cliente
        );

        // Salva la nuova fattura
        Fattura savedFattura = this.fattureRepository.save(fattura);

        // Calcola il totale fatturato annuale per il cliente
        int annoFattura = dataFattura.getYear();

        // Filtra le fatture per cliente e anno
        List<Fattura> fattureAnnuali = fattureRepository.findByClienteAndAnno(cliente, annoFattura);

        // Aggiungi la nuova fattura alla lista per calcolare il totale annuo
        if (!fattureAnnuali.contains(savedFattura)) {
            fattureAnnuali.add(savedFattura);
        }

        // Calcola il totale fatturato annuale come somma degli importi
        int totaleFatturatoAnnuale = fattureAnnuali.stream()
                .mapToInt(fatt -> (int) fatt.getImporto())
                .sum();

        // Imposta il totale fatturato annuale sul cliente e salva il cliente
        cliente.setFatturatoAnnuale(totaleFatturatoAnnuale);
        clientiRepository.save(cliente);

        // Ritorna la nuova fattura salvata
        return savedFattura;

    }

    public Fattura findById(UUID fatturaId) {
        return this.fattureRepository.findById(fatturaId).orElseThrow(() -> new NotFoundException(fatturaId));
    }

    public Fattura findByIdAndUpdate(UUID fatturaId, FatturaDTO updatedFattura) {
        UUID clienteID = null;
        try {
            clienteID = UUID.fromString(updatedFattura.clienteID());
        } catch (NumberFormatException e) {
            throw new BadRequestException("L'UUID dell' utente non è corretto");

        }

        Fattura found = findById(fatturaId);

        Cliente cliente = clientiService.findById(clienteID);
        found.setCliente(cliente);
        found.setImporto(updatedFattura.importo());
        found.setNumero(updatedFattura.numero());
        LocalDate dataFattura = null;
        try {
            dataFattura = LocalDate.parse(updatedFattura.dataFattura());
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + updatedFattura.dataFattura() + " inserire nel seguente formato: AAAA/MM/GG");
        }
        found.setDataFattura(dataFattura);
        found.setStatoFattura(updatedFattura.statoFattura());
        int annoFattura = dataFattura.getYear();
        List<Fattura> fattureAnnuali = fattureRepository.findByAnno(annoFattura);
        int totaleFatturatoAnnuale = fattureAnnuali.stream()
                .mapToInt(fatturato -> (int) fatturato.getImporto())
                .sum();
        cliente.setFatturatoAnnuale(totaleFatturatoAnnuale);
        return this.fattureRepository.save(found);
    }

    public void findByIdAndDelete(UUID fatturaId) {
        this.fattureRepository.delete(this.findById(fatturaId));
    }


    public List<Fattura> findByCliente(Cliente cliente) {
        return this.fattureRepository.findByCliente(cliente);
    }

    public List<Fattura> findByClienteId(String id) {
        UUID clienteId = null;
        try {
            clienteId = UUID.fromString(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("L'UUID del cliente non è corretto");
        }
        return this.fattureRepository.findByClienteId(clienteId);
    }

    public List<Fattura> getFattureByStato(String stato) {
        return fattureRepository.findByStatoFattura(stato);
    }

    public List<Fattura> getFattureByData(LocalDate data) {
        return fattureRepository.findByDataFattura(data);
    }


    public List<Fattura> getFattureByAnno(int anno) {
        return fattureRepository.findByAnno(anno);
    }


    public List<Fattura> getFattureByImportoRange(Double min, Double max) {
        return fattureRepository.findByImportoBetween(min, max);
    }



}
