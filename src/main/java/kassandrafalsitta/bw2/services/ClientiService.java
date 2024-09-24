package kassandrafalsitta.bw2.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.enums.Ruolo;
import kassandrafalsitta.bw2.exceptions.BadRequestException;
import kassandrafalsitta.bw2.exceptions.NotFoundException;
import kassandrafalsitta.bw2.payloads.ClientiDTO;
import kassandrafalsitta.bw2.payloads.ClientiRuoloDTO;
import kassandrafalsitta.bw2.repositories.ClientiRepository;
import kassandrafalsitta.bw2.tools.MailgunSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Service
public class ClientiService {
    @Autowired
    private ClientiRepository clientiRepository;
    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private MailgunSender mailgunSender;


    @Autowired
    private Cloudinary cloudinaryUploader;


    public Page<Cliente> findAll(int page, int size, String sortBy) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.clientiRepository.findAll(pageable);
    }

    public Cliente saveClienti(ClientiDTO body) {
        this.clientiRepository.findByEmail(body.email()).ifPresent(
                clienti -> {
                    throw new BadRequestException("L'email " + body.email() + " è già in uso!");
                }
        );

        LocalDate dataInserimento = null;
        try {
            dataInserimento = LocalDate.parse(body.dataInserimento());
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + body.dataInserimento() + " inserire nel seguente formato: AAAA/MM/GG");
        }
        LocalDate dataUltimoContatto = null;
        try {
            dataUltimoContatto = LocalDate.parse(body.dataUltimoContatto());
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + body.dataUltimoContatto() + " inserire nel seguente formato: AAAA/MM/GG");
        }
        Cliente cliente = new Cliente(
                body.username(),
                body.nome(),
                body.cognome(),
                body.email(),
                bcrypt.encode(body.password()),
                Long.parseLong(body.partitaIva()),
                dataInserimento,
                dataUltimoContatto,
                Integer.parseInt(body.fatturatoAnnuale()),
                body.pec(),
                Long.parseLong(body.telefono()),
                body.emailDiContatto(),
                body.nomeDiContatto(),
                body.cognomeDiContatto(),
                Long.parseLong(body.telefonoDiContatto()),
                body.logoAziendale(),
                body.tipoClienti());
        Cliente savedClienti = this.clientiRepository.save(cliente);

        // 4. Invio email conferma registrazione
        mailgunSender.sendRegistrationEmail(savedClienti);
        return savedClienti ;
    }

    public Cliente findById(UUID clientiId) {
        return this.clientiRepository.findById(clientiId).orElseThrow(() -> new NotFoundException(clientiId));
    }

    public Cliente findByIdAndUpdate(UUID clientiId, ClientiDTO updatedClienti) {
        Cliente found = findById(clientiId);
        found.setUsername(updatedClienti.username());
        found.setNome(updatedClienti.nome());
        found.setCognome(updatedClienti.cognome());
        found.setEmail(updatedClienti.email());
        found.setPassword(updatedClienti.password());
        LocalDate dataInserimento = null;
        try {
            dataInserimento = LocalDate.parse(updatedClienti.dataInserimento());
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + updatedClienti.dataInserimento() + " inserire nel seguente formato: AAAA/MM/GG");
        }

        found.setDataInserimento(dataInserimento);
        LocalDate dataUltimoContatto = null;
        try {
            dataUltimoContatto = LocalDate.parse(updatedClienti.dataUltimoContatto());
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + updatedClienti.dataUltimoContatto() + " inserire nel seguente formato: AAAA/MM/GG");
        }
        found.setDataUltimoContatto(dataUltimoContatto);
        found.setPartitaIva(Long.parseLong(updatedClienti.partitaIva() + "L"));
        found.setFatturatoAnnuale( Integer.parseInt(updatedClienti.fatturatoAnnuale()));
        found.setPec(updatedClienti.pec());
        found.setTelefono(Long.parseLong(updatedClienti.telefono() + "L"));
        found.setEmailDiContatto(updatedClienti.emailDiContatto());
        found.setNomeDiContatto(updatedClienti.nomeDiContatto());
        found.setCognomeDiContatto(updatedClienti.cognomeDiContatto());
        found.setTelefonoDiContatto(Long.parseLong(updatedClienti.telefonoDiContatto() + "L"));
        found.setLogoAziendale(updatedClienti.logoAziendale());
        found.setTipoClienti(updatedClienti.tipoClienti());
        return this.clientiRepository.save(found);
    }

    public Cliente findByIdAndUpdateRuolo(UUID clientiId, ClientiRuoloDTO updatedClientiRuolo) {
        Cliente found = findById(clientiId);
        Ruolo ruolo = null;
        try {
            ruolo = Ruolo.valueOf(updatedClientiRuolo.ruolo());
        } catch (Exception e) {
            throw new BadRequestException("Il formato della data non è valido: " + updatedClientiRuolo.ruolo() + " inserire nel seguente formato: AAAA/MM/GG");
        }
        found.setRuolo(ruolo);
        return this.clientiRepository.save(found);
    }

    public void findByIdAndDelete(UUID clientiId) {
        this.clientiRepository.delete(this.findById(clientiId));
    }


    public Cliente findByEmail(String email) {
        return clientiRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'utente con l'email " + email + " non è stato trovato!"));
    }

    public Cliente uploadImage(UUID authorId, MultipartFile file) throws IOException {
        Cliente found = findById(authorId);
        String avatar = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setAvatar(avatar);
        return this.clientiRepository.save(found);
    }


    // Metodo per ottenere tutti i clienti
    public List<Cliente> getAllClienti() {
        return clientiRepository.findAll();
    }

    // Metodi per ordinamento


    public List<Cliente> getClientiOrderedByFatturatoAnnuale() {
        return clientiRepository.findAllByOrderByFatturatoAnnualeDesc();
    }

    public List<Cliente> getClientiOrderedByDataInserimento() {
        return clientiRepository.findAllByOrderByDataInserimentoAsc();
    }

    public List<Cliente> getClientiOrderedByDataUltimoContatto() {
        return clientiRepository.findAllByOrderByDataUltimoContattoAsc();
    }

    // Metodi per filtraggio
    public List<Cliente> getClientiByFatturatoAnnualeRange(Long min, Long max) {
        return clientiRepository.findByFatturatoAnnualeBetween(min, max);
    }

    public List<Cliente> getClientiByDataInserimentoRange(LocalDate startDate, LocalDate endDate) {
        return clientiRepository.findByDataInserimentoBetween(startDate, endDate);
    }

    public List<Cliente> getClientiByDataUltimoContattoRange(LocalDate startDate, LocalDate endDate) {
        return clientiRepository.findByDataUltimoContattoBetween(startDate, endDate);
    }




}
