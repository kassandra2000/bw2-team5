package kassandrafalsitta.bw2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kassandrafalsitta.bw2.enums.TipoIndirizzo;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "indirizzi")

public class Indirizzo {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue
    UUID id;

    private String via;
    private String civico;
    private String localita;
    private String cap;
    @Enumerated(EnumType.STRING)
    private TipoIndirizzo tipoIndirizzo;

    @ManyToOne
    private Comune comune;

    @JsonIgnore
    @ManyToOne
    private Cliente cliente;


    public Indirizzo(String via, String civico, String localita, String cap, Comune comune, Cliente cliente, TipoIndirizzo tipoIndirizzo) {
        this.via = via;
        this.civico = civico;
        this.localita = localita;
        this.cap = cap;
        this.comune = comune;
        this.cliente = cliente;
        this.tipoIndirizzo = tipoIndirizzo;
    }
}
