package kassandrafalsitta.bw2.repositories;

import kassandrafalsitta.bw2.entities.Fattura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FatturaRepository extends JpaRepository {
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
