package kassandrafalsitta.bw2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "clienti")

public class Cliente extends Utente {

    private String ragioneSociale;
    private String partitaIva;
    private LocalDate dataInserimento;
    private LocalDate dataUltimoContatto;
    private BigInteger fatturatoAnnuale;
    private String pec;
    private String telefono;
    private String emailContatto;
    private String nomeContatto;
    private String cognomeContatto;
    private String telefonoContatto;
    private String logoAziendale;
    private String tipoClienti;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Indirizzo> indirizzi;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Fattura> fatture;

    public Cliente(UUID id, String username, String email, String password, String nome, String cognome, String avatar, String ragioneSociale, String partitaIva, LocalDate dataInserimento, LocalDate dataUltimoContatto, BigInteger fatturatoAnnuale, String pec, String telefono, String emailContatto, String nomeContatto, String cognomeContatto, String telefonoContatto, String logoAziendale, String tipoClienti, List<Indirizzo> indirizzi, List<Fattura> fatture) {
        super(id, username, email, password, nome, cognome, avatar);
        this.ragioneSociale = ragioneSociale;
        this.partitaIva = partitaIva;
        this.dataInserimento = dataInserimento;
        this.dataUltimoContatto = dataUltimoContatto;
        this.fatturatoAnnuale = fatturatoAnnuale;
        this.pec = pec;
        this.telefono = telefono;
        this.emailContatto = emailContatto;
        this.nomeContatto = nomeContatto;
        this.cognomeContatto = cognomeContatto;
        this.telefonoContatto = telefonoContatto;
        this.logoAziendale = logoAziendale;
        this.tipoClienti = tipoClienti;
        this.indirizzi = indirizzi;
        this.fatture = fatture;
    }
}
