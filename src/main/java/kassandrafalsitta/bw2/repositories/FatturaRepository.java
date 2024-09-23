package kassandrafalsitta.bw2.repositories;

import kassandrafalsitta.bw2.entities.Fattura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FatturaRepository extends JpaRepository {
    //Ordinamento fatture per id cliente
    List<Fattura> findByCliente(UUID clienteId);
    //Ordinamento fatture per stato
    List<Fattura> findByStato();
    //Ordinamento fatture per data
    List<Fattura> findByData();
    //Ordinamento fatture per anno
    List<Fattura> findByAnno();
    //Ordinamento fatture per range di importi
    List<Fattura> findByRangeImporto();
}
