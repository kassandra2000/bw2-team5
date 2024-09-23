package kassandrafalsitta.bw2.repository;

import kassandrafalsitta.bw2.entities.Indirizzo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IndirizzoRepository extends JpaRepository<Indirizzo, UUID> {}

