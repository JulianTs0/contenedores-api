package backend.grupo130.tramos.data.repository;

import backend.grupo130.tramos.data.models.RutaTraslado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PostgresRutaRepositoryI extends JpaRepository<RutaTraslado, Integer> {

}



