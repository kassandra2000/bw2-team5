package kassandrafalsitta.bw2.repositories;

import kassandrafalsitta.bw2.entities.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProvinciaRepository extends JpaRepository<Provincia, UUID> {
    Optional<Provincia> findByNome(String nome);
}
