package kassandrafalsitta.bw2.services;

import kassandrafalsitta.bw2.entities.Clienti;
import kassandrafalsitta.bw2.enums.Ruolo;
import kassandrafalsitta.bw2.exceptions.BadRequestException;
import kassandrafalsitta.bw2.exceptions.NotFoundException;
import kassandrafalsitta.bw2.payloads.ClientiDTO;
import kassandrafalsitta.bw2.payloads.ClientiRuoloDTO;
import kassandrafalsitta.bw2.repositories.ClientisRepository;
import kassandrafalsitta.bw2.tools.MailgunSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientiService {
    @Autowired
    private ClientisRepository clientiRepository;
    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private MailgunSender mailgunSender;


    public Page<Clienti> findAll(int page, int size, String sortBy) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.clientiRepository.findAll(pageable);
    }

    public Clienti saveClienti(ClientiDTO body) {
        this.clientiRepository.findByEmail(body.email()).ifPresent(
                employee -> {
                    throw new BadRequestException("L'email " + body.email() + " è già in uso!");
                }
        );
        Clienti employee = new Clienti(body.username(), body.name(), body.surname(), body.email(),bcrypt.encode(body.password()));
        Clienti savedClienti = this.clientiRepository.save(employee);

        // 4. Invio email conferma registrazione
        mailgunSender.sendRegistrationEmail(savedClienti);
        return savedClienti ;
    }

    public Clienti findById(UUID employeeId) {
        return this.clientiRepository.findById(employeeId).orElseThrow(() -> new NotFoundException(employeeId));
    }

    public Clienti findByIdAndUpdate(UUID employeeId, ClientiDTO updatedClienti) {
        Clienti found = findById(employeeId);
        found.setClientiname(updatedClienti.username());
        found.setName(updatedClienti.name());
        found.setSurname(updatedClienti.surname());
        found.setEmail(updatedClienti.email());
        found.setPassword(updatedClienti.password());
        return this.clientiRepository.save(found);
    }

    public Clienti findByIdAndUpdateRuolo(UUID employeeId, ClientiRuoloDTO updatedClientiRuolo) {
        Clienti found = findById(employeeId);
        Ruolo ruolo = null;
        try {
            ruolo = Ruolo.valueOf(updatedClientiRuolo.ruolo());
        } catch (Exception e) {
            throw new BadRequestException("Il formato della data non è valido: " + updatedClientiRuolo.ruolo() + " inserire nel seguente formato: AAAA/MM/GG");
        }
        found.setRuolo(ruolo);
        return this.clientiRepository.save(found);
    }

    public void findByIdAndDelete(UUID employeeId) {
        this.clientiRepository.delete(this.findById(employeeId));
    }


    public Clienti findByEmail(String email) {
        return clientiRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'utente con l'email " + email + " non è stato trovato!"));
    }
}
