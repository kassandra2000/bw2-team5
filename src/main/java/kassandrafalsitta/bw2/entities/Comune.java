package kassandrafalsitta.bw2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "comuni")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Comune {

    @Id
    @GeneratedValue
    private UUID id;
    private String nome;
    @ManyToOne
    @JoinColumn(name = "provincia_id")
    private Provincia provincia;

}
