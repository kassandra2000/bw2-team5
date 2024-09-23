package kassandrafalsitta.bw2.services;

import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.enums.Ruolo;
import kassandrafalsitta.bw2.exceptions.BadRequestException;
import kassandrafalsitta.bw2.exceptions.NotFoundException;
import kassandrafalsitta.bw2.payloads.ClientiDTO;
import kassandrafalsitta.bw2.payloads.ClientiRuoloDTO;
import kassandrafalsitta.bw2.repositories.ClienteRepository;
import kassandrafalsitta.bw2.tools.MailgunSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private MailgunSender mailgunSender;


    public Page<Cliente> findAll(int page, int size, String sortBy) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.clienteRepository.findAll(pageable);
    }

    public Cliente saveClienti(ClientiDTO body) {
        this.clienteRepository.findByEmail(body.email()).ifPresent(
                employee -> {
                    throw new BadRequestException("L'email " + body.email() + " è già in uso!");
                }
        );
        Cliente employee = new Cliente(body.username(), body.name(), body.surname(), body.email(),bcrypt.encode(body.password()));
        Cliente savedClienti = this.clienteRepository.save(employee);

        // 4. Invio email conferma registrazione
        mailgunSender.sendRegistrationEmail(savedCliente);
        return savedClienti ;
    }

    public Cliente findById(UUID employeeId) {
        return this.clienteRepository.findById(employeeId).orElseThrow(() -> new NotFoundException(employeeId));
    }

    public Cliente findByIdAndUpdate(UUID employeeId, ClientiDTO updatedClienti) {
        Cliente found = findById(employeeId);
        found.setClientename(updatedClienti.username());
        found.setName(updatedClienti.name());
        found.setSurname(updatedClienti.surname());
        found.setEmail(updatedClienti.email());
        found.setPassword(updatedClienti.password());
        return this.clienteRepository.save(found);
    }

    public Cliente findByIdAndUpdateRuolo(UUID employeeId, ClientiRuoloDTO updatedClientiRuolo) {
        Cliente found = findById(employeeId);
        Ruolo ruolo = null;
        try {
            ruolo = Ruolo.valueOf(updatedClientiRuolo.ruolo());
        } catch (Exception e) {
            throw new BadRequestException("Il formato della data non è valido: " + updatedClientiRuolo.ruolo() + " inserire nel seguente formato: AAAA/MM/GG");
        }
        found.setRuolo(ruolo);
        return this.clienteRepository.save(found);
    }

    public void findByIdAndDelete(UUID employeeId) {
        this.clienteRepository.delete(this.findById(employeeId));
    }


    public Cliente findByEmail(String email) {
        return clienteRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'utente con l'email " + email + " non è stato trovato!"));
    }

    public List<Cliente> getClientiOrdinePerNome(){
        return clienteRepository.findbyNome();
    }

    public List<Cliente> getClientiOrdinePerFatturatoAnnuale(){
        return clienteRepository.findByFatturatoAnnuale();
    }

    public List<Cliente> getClientiOrdinePerDataInserimento(){
        return clienteRepository.findByDataInserimento();
    }

    public List<Cliente> getClientiOrdinePerDataUltimoContatto(){
        return clienteRepository.findByUltimoContatto();
    }

    public List<Cliente> getClientiOrdinePerProvinciaSedeLegale(){
        return clienteRepository.findByProvinciaSedeLegale();
    }

    public List<Cliente> getClientiFiltraPerNome(String nome){
        return clienteRepository.filterByNome(nome);
    }

    public List<Cliente> getClientiFiltraPerFatturatoAnnuale(BigDecimal minimo, BigDecimal massimo){
        return clienteRepository.filterByFatturatoAnnuale(minimo, massimo);
    }

    public List<Cliente> getClientiFiltraPerDataInserimento(LocalDate startDate, LocalDate endDate){
        return clienteRepository.filterByDataInserimento(startDate, endDate);
    }

    public List<Cliente> getClientiFiltraPerUltimoContatto(LocalDate startDate, LocalDate endDate){
        return clienteRepository.filterByUltimoContatto(startDate, endDate);
    }

    public List<Cliente> getClientiFiltraPerProvinciaSedeLegale(){
        return clienteRepository.filterByProvinciaSedeLegale();
    }



}
