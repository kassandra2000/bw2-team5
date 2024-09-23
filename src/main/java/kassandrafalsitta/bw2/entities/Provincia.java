package kassandrafalsitta.bw2.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "province")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Provincia {

    @Id
    @GeneratedValue

    private UUID id;

    private String nome;
    private String sigla;
    private String regione;

}
