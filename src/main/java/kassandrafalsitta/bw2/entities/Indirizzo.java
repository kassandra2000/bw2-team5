package kassandrafalsitta.bw2.entities;

import jakarta.persistence.*;
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

    @ManyToOne
    private Comune comune;

    @ManyToOne
    private Cliente cliente;

    public Indirizzo(String via, String civico, String localita, String cap, String comune) {
    }

    public Indirizzo(String via, String civico, String localita, String cap, Comune comune) {
    }
}
