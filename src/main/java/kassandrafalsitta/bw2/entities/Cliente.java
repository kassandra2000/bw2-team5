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
@AllArgsConstructor
@ToString
@Table(name = "clienti")

public class Cliente {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID id;

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
}
