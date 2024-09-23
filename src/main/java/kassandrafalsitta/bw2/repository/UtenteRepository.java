package kassandrafalsitta.bw2.repository;

import kassandrafalsitta.bw2.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteRepository extends JpaRepository<Utente, Long> {}
