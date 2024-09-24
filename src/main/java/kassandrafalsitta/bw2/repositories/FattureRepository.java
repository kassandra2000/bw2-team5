package kassandrafalsitta.bw2.repositories;

import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.entities.Fattura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FattureRepository extends JpaRepository<Fattura, UUID> {

    Optional<Fattura> findByNumeroAndCliente(String numero, Cliente cliente);

    // Filtraggio per cliente
    List<Fattura> findByCliente(Cliente cliente);

    // Filtraggio per stato
    List<Fattura> findByStato(Fattura statoFattura);

    // Filtraggio per data
    List<Fattura> findByData(LocalDate data);

    // Filtraggio per anno
    List<Fattura> findByAnno(Integer anno);

    // Filtraggio per range di importi
    List<Fattura> findByImportoBetween(Double min, Double max);

}

