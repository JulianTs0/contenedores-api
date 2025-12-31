package backend.grupo130.envios.data.repository;

import backend.grupo130.envios.data.models.PreciosNegocioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostgresPreciosNegocioRepositoryI extends JpaRepository<PreciosNegocioModel, Long> {

    @Query("SELECT p FROM PreciosNegocioModel p ORDER BY p.fechaCreacion DESC LIMIT 1")
    Optional<PreciosNegocioModel> findTopByOrderByFechaCreacionDesc();
}
