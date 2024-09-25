package kassandrafalsitta.bw2.repositories;

import kassandrafalsitta.bw2.entities.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, UUID> {
}
