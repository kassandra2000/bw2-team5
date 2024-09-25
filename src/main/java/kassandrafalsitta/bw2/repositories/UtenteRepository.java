package kassandrafalsitta.bw2.repositories;

import kassandrafalsitta.bw2.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UtenteRepository extends JpaRepository <Utente, UUID>{
}
