package kassandrafalsitta.bw2.repositories;

import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.entities.Indirizzo;
import kassandrafalsitta.bw2.enums.TipoIndirizzo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IndirizzoRepository extends JpaRepository<Indirizzo, UUID> {

    Optional<Indirizzo> findByClienteAndTipoIndirizzo(Cliente cliente, TipoIndirizzo tipoIndirizzo);

}

