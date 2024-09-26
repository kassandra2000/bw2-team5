package kassandrafalsitta.bw2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "fatture")

public class Fattura {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue
    UUID id;

    private LocalDate dataFattura;
    private double importo;
    private String numero;
    private String statoFattura;


    @ManyToOne
    private Cliente cliente;

    public Fattura(LocalDate dataFattura, double importo, String numero, String statoFattura, Cliente cliente) {
        this.dataFattura = dataFattura;
        this.importo = importo;
        this.numero = numero;
        this.statoFattura = statoFattura;
        this.cliente = cliente;
    }
}
