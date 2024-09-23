package kassandrafalsitta.bw2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "comuni")

public class Comune {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue
    UUID id;

    private String nome;
    @ManyToOne
    private Provincia provincia;

    @OneToMany(mappedBy = "comune")
    private List<Indirizzo> indirizzi;
}
