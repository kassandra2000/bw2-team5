package kassandrafalsitta.bw2.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "clienti")
@JsonIgnoreProperties({"password", "ruolo", "authorities", "enabled", "accountNonLocked", "accountNonExpired", "credentialsNonExpired"})
public class Cliente extends Utente implements UserDetails {
    private long partitaIva;
    private LocalDate dataInserimento;
    private LocalDate dataUltimoContatto;
    private int fatturatoAnnuale;
    private String pec;
    private long telefono;
    private String emailDiContatto;
    private long telefonoDiContatto;
    private String logoAziendale;
    private String tipoClienti;

    @OneToOne(cascade = CascadeType.REMOVE,orphanRemoval = true)
    private Indirizzo sedeLegale;

    @OneToOne(cascade = CascadeType.REMOVE,orphanRemoval = true)
    private Indirizzo sedeOperativa;

    public Cliente(String username, String nome, String cognome, String email, String password, long partitaIva, LocalDate dataInserimento, LocalDate dataUltimoContatto, String pec, long telefono, String emailDiContatto, long telefonoDiContatto, String logoAziendale, String tipoClienti) {
        super(username, nome, cognome, email, password);
        this.partitaIva = partitaIva;
        this.dataInserimento = dataInserimento;
        this.dataUltimoContatto = dataUltimoContatto;
        this.fatturatoAnnuale = 0;
        this.pec = pec;
        this.telefono = telefono;
        this.emailDiContatto = emailDiContatto;
        this.telefonoDiContatto = telefonoDiContatto;
        this.logoAziendale = logoAziendale;
        this.tipoClienti = tipoClienti;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRuolo().name()));
    }
}
