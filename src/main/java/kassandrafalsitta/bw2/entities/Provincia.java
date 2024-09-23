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
@Table(name = "province")

public class Provincia {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID id;

    private String nome;
    private String sigla;

    @OneToMany(mappedBy = "provincia")
    private List<Comune> comuni;
}
