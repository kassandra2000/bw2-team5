package kassandrafalsitta.bw2.services;

import kassandrafalsitta.bw2.entities.Indirizzo;
import kassandrafalsitta.bw2.exceptions.NotFoundException;
import kassandrafalsitta.bw2.payloads.IndirizzoDTO;
import kassandrafalsitta.bw2.repositories.ComuneRepository;
import kassandrafalsitta.bw2.repository.IndirizzoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IndirizziService {

    @Autowired
    private IndirizzoRepository indirizziRepository;

    @Autowired
    private ComuneRepository comuneRepository;

    public List<Indirizzo> findAll() {
        return indirizziRepository.findAll();
    }

    public Indirizzo saveIndirizzo(IndirizzoDTO body) {
        Indirizzo indirizzo = new Indirizzo(
                body.via(),
                body.civico(),
                body.localita(),
                body.cap(),
                body.comune()
        );
        return indirizziRepository.save(indirizzo);
    }

    public Indirizzo findById(UUID indirizzoId) {
        return indirizziRepository.findById(indirizzoId)
                .orElseThrow(() -> new NotFoundException(indirizzoId));
    }

    public Indirizzo findByIdAndUpdate(UUID indirizzoId, IndirizzoDTO updatedIndirizzo) {
        Indirizzo found = findById(indirizzoId);

        found.setVia(updatedIndirizzo.via());
        found.setCivico(updatedIndirizzo.civico());
        found.setLocalita(updatedIndirizzo.localita());
        found.setCap(updatedIndirizzo.cap());
        found.setComune(updatedIndirizzo.comune());

        return indirizziRepository.save(found);
    }

    public void findByIdAndDelete(UUID indirizzoId) {
        indirizziRepository.delete(this.findById(indirizzoId));
    }
}

