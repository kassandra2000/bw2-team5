package kassandrafalsitta.bw2.entities;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "fatture")

public class Fattura {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue
    UUID id;

    private LocalDate data;
    private double importo;
    private String numero;
    private String statoFattura;


    @ManyToOne
    private Cliente cliente;
}
