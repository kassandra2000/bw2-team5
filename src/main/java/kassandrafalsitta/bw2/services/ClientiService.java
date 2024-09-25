package kassandrafalsitta.bw2.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.entities.Indirizzo;
import kassandrafalsitta.bw2.entities.Provincia;
import kassandrafalsitta.bw2.enums.Ruolo;
import kassandrafalsitta.bw2.exceptions.BadRequestException;
import kassandrafalsitta.bw2.exceptions.NotFoundException;
import kassandrafalsitta.bw2.payloads.ClientiDTO;
import kassandrafalsitta.bw2.payloads.ClientiRuoloDTO;
import kassandrafalsitta.bw2.payloads.ClientiUpdateDTO;
import kassandrafalsitta.bw2.repositories.ClientiRepository;
import kassandrafalsitta.bw2.repositories.IndirizzoRepository;
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

    @Autowired
    private IndirizzoRepository indirizziRepository;


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

        UUID sedeLegaleId;
        try {
            sedeLegaleId = UUID.fromString(body.sedeLegaleId());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("L'ID del sedeLegale non è valido: " + body.sedeLegaleId());
        }
        UUID sedeOperativaId;
        try {
            sedeOperativaId = UUID.fromString(body.sedeOperativaId());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("L'ID delsedeOperativa non è valido: " + body.sedeOperativaId());
        }

        Indirizzo sedeLegale = indirizziRepository.findById(sedeLegaleId) .orElseThrow(() -> new NotFoundException("Indirizzo non trovato con ID: " + body.sedeLegaleId()));;
        Indirizzo sedeOperativa = indirizziRepository.findById(sedeOperativaId) .orElseThrow(() -> new NotFoundException("Indirizzo non trovato con ID: " + body.sedeOperativaId()));;

        Cliente cliente = new Cliente(
                body.username(),
                body.nome(),
                body.cognome(),
                body.email(),
                bcrypt.encode(body.password()),
                Long.parseLong(body.partitaIva()),
                dataInserimento,
                dataUltimoContatto,
                body.pec(),
                Long.parseLong(body.telefono()),
                body.emailDiContatto(),
                Long.parseLong(body.telefonoDiContatto()),
                body.logoAziendale(),
                body.tipoClienti(),
                sedeLegale,
                sedeOperativa
        );
        Cliente savedClienti = this.clientiRepository.save(cliente);

        // 4. Invio email conferma registrazione
        mailgunSender.sendRegistrationEmail(savedClienti);
        return savedClienti ;
    }

    public Cliente findById(UUID clientiId) {
        return this.clientiRepository.findById(clientiId).orElseThrow(() -> new NotFoundException(clientiId));
    }

    public Cliente findByIdAndUpdate(UUID clientiId, ClientiUpdateDTO updatedClienti) {
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
        found.setTelefonoDiContatto(Long.parseLong(updatedClienti.telefonoDiContatto() + "L"));
        found.setLogoAziendale(updatedClienti.logoAziendale());
        found.setTipoClienti(updatedClienti.tipoClienti());
        UUID sedeLegaleId;
        try {
            sedeLegaleId = UUID.fromString(updatedClienti.sedeLegaleId());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("L'ID del sedeLegale non è valido: " + updatedClienti.sedeLegaleId());
        }
        UUID sedeOperativaId;
        try {
            sedeOperativaId = UUID.fromString(updatedClienti.sedeOperativaId());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("L'ID delsedeOperativa non è valido: " + updatedClienti.sedeOperativaId());
        }
        Indirizzo sedeLegale = indirizziRepository.findById(sedeLegaleId) .orElseThrow(() -> new NotFoundException("Indirizzo non trovato con ID: " + updatedClienti.sedeLegaleId()));;
        Indirizzo sedeOperativa = indirizziRepository.findById(sedeOperativaId) .orElseThrow(() -> new NotFoundException("Indirizzo non trovato con ID: " + updatedClienti.sedeOperativaId()));;
        found.setSedeLegale(sedeLegale);
        found.setSedeOperativa(sedeOperativa);
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


    public List<Cliente> getClientiByFatturatoAnnuale(int fatturatoAnnuale) {
        return clientiRepository.findAllByFatturatoAnnuale(fatturatoAnnuale);
    }

    public List<Cliente> getClientiByDataInserimento(String dataInserimento) {
        LocalDate data  = null;
        try {
            data = LocalDate.parse(dataInserimento);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + dataInserimento + " inserire nel seguente formato: AAAA/MM/GG");
        }
        return clientiRepository.findAllByDataInserimento(data);
    }

    public List<Cliente> getClientiByDataUltimoContatto(String dataUltimoContatto) {
        LocalDate data  = null;
        try {
            data = LocalDate.parse(dataUltimoContatto);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + dataUltimoContatto + " inserire nel seguente formato: AAAA/MM/GG");
        }
        return clientiRepository.findAllByDataUltimoContatto(data);
    }

    // Metodi per filtraggio
    public List<Cliente> getClientiByFatturatoAnnualeRange(Long min, Long max) {
        return clientiRepository.findByFatturatoAnnualeBetween(min, max);
    }

    public List<Cliente> getClientiByDataInserimentoRange(String startDate, String endDate) {
        LocalDate data  = null;
        try {
            data = LocalDate.parse(startDate);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + startDate + " inserire nel seguente formato: AAAA/MM/GG");
        }
        LocalDate data1  = null;
        try {
            data1 = LocalDate.parse(endDate);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + endDate + " inserire nel seguente formato: AAAA/MM/GG");
        }
        return clientiRepository.findByDataInserimentoBetween(data, data1);
    }

    public List<Cliente> getClientiByDataUltimoContattoRange(String startDate, String endDate) {
        LocalDate data  = null;
        try {
            data = LocalDate.parse(startDate);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + startDate + " inserire nel seguente formato: AAAA/MM/GG");
        }
        LocalDate data1  = null;
        try {
            data1 = LocalDate.parse(endDate);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + endDate + " inserire nel seguente formato: AAAA/MM/GG");
        }
        return clientiRepository.findByDataUltimoContattoBetween(data, data1);
    }

    public List<Cliente> getClientiByNomeParziale(String nomeParziale) {
        return clientiRepository.findByNomeContainingIgnoreCase(nomeParziale);
    }


    public List<Cliente> getAllClientiByNomeAsc() {
        return clientiRepository.findAllByOrderByNomeAsc();
    }


    public List<Cliente> getAllClientiByFatturatoAnnualeAsc() {
        return clientiRepository.findAllByOrderByFatturatoAnnualeAsc();
    }


    public List<Cliente> getAllClientiByDataInserimentoAsc() {
        return clientiRepository.findAllByOrderByDataInserimentoAsc();
    }


    public List<Cliente> getAllClientiByDataUltimoContattoAsc() {
        return clientiRepository.findAllByOrderByDataUltimoContattoAsc();
    }


    public List<Cliente> getAllClientiBySedeLegaleProvinciaAsc() {
        return clientiRepository.findAllByOrderBySedeLegale_Comune_ProvinciaAsc();
    }

}
