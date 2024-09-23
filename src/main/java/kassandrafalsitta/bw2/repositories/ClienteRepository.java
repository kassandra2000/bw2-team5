package kassandrafalsitta.bw2.repositories;

import kassandrafalsitta.bw2.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    List<Cliente> findByEmail(String email);
    //Ordinamento per Nome
    List<Cliente> findbyNome();
    //Ordinamento per fatturato annuale
    List<Cliente> findByFatturatoAnnuale();
    //Ordinamento per data di inserimento
    List<Cliente> findByDataInserimento();
    //Ordinamento per data di ultimo contatto
    List<Cliente> findByUltimoContatto();
    //Ordinamento per provincia della sede legale
    List<Cliente> findByProvinciaSedeLegale();
    //Filtro per nome
    List<Cliente> filterByNome(String nome);
    //Filtro per Fatturato annuale
    List<Cliente> filterByFatturatoAnnuale(BigDecimal minimo, BigDecimal massimo);
    //Filtro per data di inserimento
    List<Cliente> filterByDataInserimento(LocalDate startDate, LocalDate endDate);
    //Filtro per data di ultimo contatto
    List<Cliente> filterByUltimoContatto(LocalDate startDate, LocalDate endDate);
    //Filtro per provincia della sede legale
    List<Cliente> filterByProvinciaSedeLegale();

}
