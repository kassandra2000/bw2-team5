package kassandrafalsitta.bw2.repositories;

import kassandrafalsitta.bw2.entities.Indirizzo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IndirizzoRepository extends JpaRepository<Indirizzo, UUID> {

    Optional<Indirizzo> findByCliente_sedeLegale(Indirizzo sedeLegale);

    Optional<Indirizzo> findByCliente_sedeOperativa(Indirizzo sedeOperativa);
}

