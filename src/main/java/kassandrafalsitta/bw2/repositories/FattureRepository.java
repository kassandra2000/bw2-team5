package kassandrafalsitta.bw2.repositories;

import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.entities.Fattura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface FattureRepository extends JpaRepository<Fattura, UUID> {

    @Query("SELECT f FROM Fattura f WHERE f.cliente = :cliente AND YEAR(f.dataFattura) = :anno")
    List<Fattura> findByClienteAndAnno(@Param("cliente") Cliente cliente, @Param("anno") int anno);

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

