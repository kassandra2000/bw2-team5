package kassandrafalsitta.bw2.repositories;

import kassandrafalsitta.bw2.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientiRepository extends JpaRepository<Cliente, UUID> {
    Optional<Cliente> findByEmail(String email);

    //Ordinamento per Nome
    List<Cliente> findbyNome();

    //Ordinamento per fatturato annuale
    List<Cliente> findByFatturatoAnnuale();

    //Ordinamento per data di inserimento
    List<Cliente> findByDataInserimento();

    //Ordinamento per data di ultimo contatto
    List<Cliente> findByDataUltimoContatto();

    //Ordinamento per provincia della sede legale
    List<Cliente> findByIndirizzo_TipoDiIndirizzo();

    //Filtro per nome
    List<Cliente> findByNome(String nome);

    //Filtro per Fatturato annuale
    List<Cliente> findByFatturatoAnnuale(BigDecimal minimo, BigDecimal massimo);

    //Filtro per data di inserimento
    List<Cliente> findByDataInserimento(LocalDate startDate, LocalDate endDate);

    //Filtro per data di ultimo contatto
    List<Cliente> findByUltimoContatto(LocalDate startDate, LocalDate endDate);

    //Filtro per provincia della sede legale
    List<Cliente> findByProvinciaSedeLegale();
}
