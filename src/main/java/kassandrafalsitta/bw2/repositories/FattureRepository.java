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
    List<Fattura> findByCliente(Cliente cliente);
    Optional<Fattura> findByNumeroAndCliente(String numero,Cliente cliente);

    //filtro fatture per id cliente
    List<Fattura> filterByCliente(UUID clienteId);
    //filtro fatture per stato
    List<Fattura> filterByStato(String stato);
    //filtro fatture per data
    List<Fattura> filterByData(LocalDate data);
    //filtro fatture per anno
    List<Fattura> filterByAnno(LocalDate startDate, LocalDate endDate);
    //filtro fatture per range di importi
    List<Fattura> filterByRangeImporto(BigDecimal minimo, BigDecimal massimo);
}

