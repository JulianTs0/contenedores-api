package backend.grupo130.tramos.data.repository;

import backend.grupo130.tramos.data.models.RutaTraslado;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostgresRutaRepositoryI extends JpaRepository<RutaTraslado, Integer> {
}



