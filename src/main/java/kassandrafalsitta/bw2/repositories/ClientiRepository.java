package kassandrafalsitta.bw2.repositories;

import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.entities.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientiRepository extends JpaRepository<Cliente, UUID> {
    Optional<Cliente> findByEmail(String email);

    // Ordinamento per fatturato annuale
    List<Cliente> findAllByFatturatoAnnuale(int fatturatoAnnuale);

    // Ordinamento per data di inserimento
    List<Cliente> findAllByDataInserimento(LocalDate dataInserimento);

    // Ordinamento per data di ultimo contatto
    List<Cliente> findAllByDataUltimoContatto(LocalDate dataUltimoContatto);

    // Filtraggio per fatturato annuale (range)
    List<Cliente> findByFatturatoAnnualeBetween(Long min, Long max);

    // Filtraggio per data di inserimento
    List<Cliente> findByDataInserimentoBetween(LocalDate startDate, LocalDate endDate);

    // Filtraggio per data di ultimo contatto
    List<Cliente> findByDataUltimoContattoBetween(LocalDate startDate, LocalDate endDate);

    //Filtraggio per parte del nome
    List<Cliente> findByNomeContainingIgnoreCase(String nomeParziale);

    //order
    List<Cliente> findAllByOrderByNomeAsc();
    List<Cliente> findAllByOrderByFatturatoAnnualeAsc();
    List<Cliente> findAllByOrderByDataInserimentoAsc();
    List<Cliente> findAllByOrderByDataUltimoContattoAsc();
    List<Cliente> findAllByOrderBySedeLegale_Comune_ProvinciaAsc();


}
