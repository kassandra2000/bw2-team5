package kassandrafalsitta.bw2.repositories;

import kassandrafalsitta.bw2.entities.Cliente;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    //Ordinamento per Nome
    List<Cliente> findAll(Sort sort);
    //Ordinamento per fatturato annuale
    //Ordinamento per data di inserimento
    //Ordinamento per data di ultimo contatto
    //Ordinamento per provincia della sede legale
    //Filtro per Fatturato annuale
    //Filtro per data di inserimento
    //Filtro per data di ultimo contatto
    //Filtro per provincia della sede legale
}
