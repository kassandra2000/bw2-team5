package kassandrafalsitta.bw2.services;

import kassandrafalsitta.bw2.entities.Utente;
import kassandrafalsitta.bw2.enums.Ruolo;
import kassandrafalsitta.bw2.exceptions.BadRequestException;
import kassandrafalsitta.bw2.exceptions.NotFoundException;
import kassandrafalsitta.bw2.payloads.ClientiRuoloDTO;
import kassandrafalsitta.bw2.payloads.UtenteDTO;
import kassandrafalsitta.bw2.repositories.UtentiRepository;
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
public class UtenteService {
    @Autowired
    UtentiRepository utentiRepository;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private MailgunSender mailgunSender;


    public Page<Utente> findAll(int page, int size, String sortBy) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.utentiRepository.findAll(pageable);
    }

    public Utente saveUtente(UtenteDTO body) {
        this.utentiRepository.findByEmail(body.email()).ifPresent(
                employee -> {
                    throw new BadRequestException("L'email " + body.email() + " è già in uso!");
                }
        );
        Utente employee = new Utente(body.username(), body.nome(), body.cognome(), body.email(),bcrypt.encode(body.password()));
        Utente savedUtente = this.utentiRepository.save(employee);

        // 4. Invio email conferma registrazione
        mailgunSender.sendRegistrationEmailUtente(savedUtente);
        return savedUtente ;
    }

    public Utente findByIdUser(UUID utenteId) {
        return this.utentiRepository.findById(utenteId).orElseThrow(() -> new NotFoundException(utenteId));
    }

    public Utente findByIdAndUpdate(UUID utenteId, UtenteDTO updatedUtente) {
        Utente found = findByIdUser(utenteId);
        System.out.println(found);
        found.setUsername(updatedUtente.username());
        found.setNome(updatedUtente.nome());
        found.setCognome(updatedUtente.cognome());
        found.setEmail(updatedUtente.email());
        found.setPassword(updatedUtente.password());
        return this.utentiRepository.save(found);
    }

    public Utente findByIdAndUpdateRuolo(UUID utenteId, ClientiRuoloDTO updatedUtenteRuolo) {
        Utente found = findByIdUser(utenteId);
        Ruolo role = null;
        try {
            role = Ruolo.valueOf(updatedUtenteRuolo.ruolo());
        } catch (Exception e) {
            throw new BadRequestException("Il formato della data non è valido: " + updatedUtenteRuolo.ruolo() + " inserire nel seguente formato: AAAA-MM-GG");
        }
        found.setRuolo(role);
        return this.utentiRepository.save(found);
    }

    public void findByIdAndDelete(UUID utenteId) {
        this.utentiRepository.delete(this.findByIdUser(utenteId));
    }


    public Utente findByEmail(String email) {
        return utentiRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'utente con l'email " + email + " non è stato trovato!"));
    }
}
