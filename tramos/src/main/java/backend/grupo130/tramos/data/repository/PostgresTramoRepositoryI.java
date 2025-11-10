package backend.grupo130.tramos.data.repository;

import backend.grupo130.tramos.data.models.Tramo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostgresTramoRepositoryI extends JpaRepository<Tramo, Integer> {
}



