package kassandrafalsitta.bw2.repository;
import kassandrafalsitta.bw2.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {}

