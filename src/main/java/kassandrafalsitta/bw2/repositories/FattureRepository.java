package kassandrafalsitta.bw2.repositories;

import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.entities.Fattura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FattureRepository extends JpaRepository<Fattura, UUID> {
    List<Fattura> findByCliente(Cliente cliente);
    Optional<Fattura> findByNumeroAndCliente(String numero,Cliente cliente);

}
