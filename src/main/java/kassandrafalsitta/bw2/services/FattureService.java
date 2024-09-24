package kassandrafalsitta.bw2.services;

import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.entities.Fattura;
import kassandrafalsitta.bw2.exceptions.BadRequestException;
import kassandrafalsitta.bw2.exceptions.NotFoundException;
import kassandrafalsitta.bw2.payloads.FatturaDTO;
import kassandrafalsitta.bw2.repositories.FattureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public Page<Fattura> findAll(int page, int size, String sortBy) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.fattureRepository.findAll(pageable);
    }

    public Fattura saveFattura(FatturaDTO body) {
        UUID clienteID = null;
        try {
            clienteID = UUID.fromString(body.clienteID());
        } catch (NumberFormatException e) {
            throw new BadRequestException("L'UUID dell' utente non è corretto");
        }


        Cliente cliente = clientiService.findById(clienteID);
        Optional<Fattura> existingFatture = this.fattureRepository.findByNumeroAndCliente(body.numero(), cliente);
        this.fattureRepository.findByNumeroAndCliente(body.numero(), cliente).ifPresent(
                fattura -> {
                    throw new BadRequestException("La fattura numero " + body.numero() + " è già in uso per l'utente " + cliente.getId());
                }
        );

        LocalDate dataFattura = null;
        try {
            dataFattura = LocalDate.parse(body.dataFattura());
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + body.dataFattura() + " inserire nel seguente formato: AAAA/MM/GG");
        }
        Fattura fattura = new Fattura(dataFattura, body.importo(), body.numero(), body.statoFattura(), cliente);
        return this.fattureRepository.save(fattura);

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
        ;
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
        return this.fattureRepository.save(found);
    }

    public void findByIdAndDelete(UUID fatturaId) {
        this.fattureRepository.delete(this.findById(fatturaId));
    }


    public List<Fattura> findByCliente(Cliente cliente) {
        return this.fattureRepository.findByCliente(cliente);
    }

    public List<Fattura> getFattureFiltraPerStato(String stato){
        return fattureRepository.findByStato(stato);
    }

    public List<Fattura> getFattureFiltraPerData(LocalDate data){
        return fattureRepository.findByData(data);
    }

    public List<Fattura> getFattureFiltraPerAnno(LocalDate startDate, LocalDate endDate){
        return fattureRepository.findByAnno(startDate, endDate);
    }

    public List<Fattura> getFattureFiltraPerRangeImporti(BigDecimal minimo, BigDecimal massimo){
        return fattureRepository.findByRangeImporto(minimo, massimo);
    }

}
