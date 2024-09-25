package kassandrafalsitta.bw2.repositories;

import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.entities.Fattura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FattureRepository extends JpaRepository<Fattura, UUID> {

    Optional<Fattura> findByNumeroAndCliente(String numero, Cliente cliente);

    // Filtraggio per cliente
    List<Fattura> findByCliente(Cliente cliente);

    //Filtraggio per cliente ID
    List<Fattura> findByClienteId(UUID id);

    // Filtraggio per stato
    List<Fattura> findByStatoFattura(String statoFattura);

    // Filtraggio per data
    List<Fattura> findByDataFattura(LocalDate data);


    // Filtraggio per anno
    @Query("SELECT e FROM Fattura e WHERE YEAR(e.dataFattura) = :year")
    List<Fattura> findByAnno(@Param("year") int year);


    // Filtraggio per range di importi
    List<Fattura> findByImportoBetween(Double min, Double max);

}

