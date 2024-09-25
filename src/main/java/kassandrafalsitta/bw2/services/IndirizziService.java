package kassandrafalsitta.bw2.services;

import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.entities.Comune;
import kassandrafalsitta.bw2.entities.Indirizzo;
import kassandrafalsitta.bw2.exceptions.BadRequestException;
import kassandrafalsitta.bw2.exceptions.NotFoundException;
import kassandrafalsitta.bw2.payloads.IndirizzoDTO;
import kassandrafalsitta.bw2.repositories.ComuneRepository;
import kassandrafalsitta.bw2.repositories.IndirizzoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IndirizziService {

    @Autowired
    private IndirizzoRepository indirizzoRepository;

    @Autowired
    private ComuneRepository comuneRepository;

    @Autowired
    private ClientiService clientiService;


    public List<Indirizzo> findAll() {
        return indirizzoRepository.findAll();
    }

    public Indirizzo saveIndirizzo(IndirizzoDTO body) {
        // Verifica che l'ID del comune sia valido e converte da String a UUID
        UUID comuneId;
        try {
            comuneId = UUID.fromString(body.comuneId());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("L'ID del comune non è valido: " + body.comuneId());
        }

        Comune comune = comuneRepository.findById(comuneId)
                .orElseThrow(() -> new NotFoundException("Comune non trovato con ID: " + comuneId));

        UUID clienteId;
        try {
            clienteId = UUID.fromString(body.clienteId());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("L'ID del cliente non è valido: " + body.clienteId());
        }

        Cliente cliente = clientiService.findById(clienteId);
        this.indirizzoRepository.findByCliente_sedeLegale(cliente.getSedeLegale()).ifPresent(
                clienti -> {
                    throw new BadRequestException("La sede legalel " + cliente.getSedeLegale() + " è già in uso!");
                }
        );

        this.indirizzoRepository.findByCliente_sedeOperativa(cliente.getSedeOperativa()).ifPresent(
                clienti -> {
                    throw new BadRequestException("La sede operativa " + cliente.getSedeOperativa() + " è già in uso!");
                }
        );

        Indirizzo indirizzo = new Indirizzo(
                body.via(),
                body.civico(),
                body.localita(),
                body.cap(),
                comune
        );
        return indirizzoRepository.save(indirizzo);



    }

    public Indirizzo findById(UUID indirizzoId) {
        return indirizzoRepository.findById(indirizzoId)
                .orElseThrow(() -> new NotFoundException("Indirizzo non trovato con ID: " + indirizzoId));
    }

    public Indirizzo findByIdAndUpdate(UUID indirizzoId, IndirizzoDTO updatedIndirizzo) {
        Indirizzo indirizzo = findById(indirizzoId);

        UUID comuneId;
        try {
            comuneId = UUID.fromString(updatedIndirizzo.comuneId());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("L'ID del comune non è valido: " + updatedIndirizzo.comuneId());
        }

        Comune comune = comuneRepository.findById(comuneId)
                .orElseThrow(() -> new NotFoundException("Comune non trovato con ID: " + comuneId));

        indirizzo.setVia(updatedIndirizzo.via());
        indirizzo.setCivico(updatedIndirizzo.civico());
        indirizzo.setLocalita(updatedIndirizzo.localita());
        indirizzo.setCap(updatedIndirizzo.cap());
        indirizzo.setComune(comune);

        return indirizzoRepository.save(indirizzo);
    }

    public void findByIdAndDelete(UUID indirizzoId) {
        Indirizzo indirizzo = findById(indirizzoId);
        indirizzoRepository.delete(indirizzo);
    }
}
