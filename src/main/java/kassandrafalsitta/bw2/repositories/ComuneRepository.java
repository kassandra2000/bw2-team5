package kassandrafalsitta.bw2.repositories;

import kassandrafalsitta.bw2.entities.Comune;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ComuneRepository extends JpaRepository<Comune, UUID> {
}
