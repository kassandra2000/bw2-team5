package kassandrafalsitta.bw2.services;

import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.entities.Comune;
import kassandrafalsitta.bw2.entities.Indirizzo;
import kassandrafalsitta.bw2.enums.TipoIndirizzo;
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


        // Determina il tipo di indirizzo
        TipoIndirizzo tipoIndirizzo;
        try {
            tipoIndirizzo = TipoIndirizzo.valueOf(body.tipoIndirizzo());
        } catch (Exception e) {
            throw new BadRequestException("Il tipo di indirizzo non è valido: " + body.tipoIndirizzo() +
                    " Inserire 'SEDE_LEGALE' o 'SEDE_OPERATIVA'");
        }
        // Creazione dell'indirizzo
        Indirizzo indirizzo = new Indirizzo(
                body.via(),
                body.civico(),
                body.localita(),
                body.cap(),
                comune,
                cliente,
                tipoIndirizzo
        );
        // Controlla se il cliente ha già una sede legale
        if (tipoIndirizzo == TipoIndirizzo.SEDE_LEGALE) {
            this.indirizzoRepository.findByClienteAndTipoIndirizzo(cliente, TipoIndirizzo.SEDE_LEGALE).ifPresent(
                    indirizzi -> {
                        throw new BadRequestException("Il cliente ha già una sede legale registrata: " + indirizzo.getVia());
                    }
            );
        } else {
            cliente.setSedeLegale(indirizzo);
        }

        // Controlla se il cliente ha già una sede operativa
        if (tipoIndirizzo == TipoIndirizzo.SEDE_OPERATIVA) {
            this.indirizzoRepository.findByClienteAndTipoIndirizzo(cliente, TipoIndirizzo.SEDE_OPERATIVA).ifPresent(
                    indirizzi -> {
                        throw new BadRequestException("Il cliente ha già una sede operativa registrata: " + indirizzo.getVia());
                    }
            );
        } else {
            cliente.setSedeOperativa(indirizzo);
        }

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
        UUID clienteId;
        try {
            clienteId = UUID.fromString(updatedIndirizzo.clienteId());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("L'ID del cliente non è valido: " + updatedIndirizzo.clienteId());
        }

        Cliente cliente = clientiService.findById(clienteId);
        indirizzo.setCliente(cliente);
        TipoIndirizzo tipoIndirizzo;
        try {
            tipoIndirizzo = TipoIndirizzo.valueOf(updatedIndirizzo.tipoIndirizzo());
        } catch (Exception e) {
            throw new BadRequestException("Il tipo di indirizzo non è valido: " + updatedIndirizzo.tipoIndirizzo() +
                    " Inserire 'SEDE_LEGALE' o 'SEDE_OPERATIVA'");
        }
        if (tipoIndirizzo == TipoIndirizzo.SEDE_LEGALE) {
            this.indirizzoRepository.findByClienteAndTipoIndirizzo(cliente, TipoIndirizzo.SEDE_LEGALE).ifPresent(
                    indirizzi -> {
                        throw new BadRequestException("Il cliente ha già una sede legale registrata: " + indirizzo.getVia());
                    }
            );
        } else {
            cliente.setSedeLegale(indirizzo);
        }

        // Controlla se il cliente ha già una sede operativa
        if (tipoIndirizzo == TipoIndirizzo.SEDE_OPERATIVA) {
            this.indirizzoRepository.findByClienteAndTipoIndirizzo(cliente, TipoIndirizzo.SEDE_OPERATIVA).ifPresent(
                    indirizzi -> {
                        throw new BadRequestException("Il cliente ha già una sede operativa registrata: " + indirizzo.getVia());
                    }
            );
        } else {
            cliente.setSedeOperativa(indirizzo);
        }
        return indirizzoRepository.save(indirizzo);
    }

    public void findByIdAndDelete(UUID indirizzoId) {
        Indirizzo indirizzo = findById(indirizzoId);
        indirizzoRepository.delete(indirizzo);
    }
}
