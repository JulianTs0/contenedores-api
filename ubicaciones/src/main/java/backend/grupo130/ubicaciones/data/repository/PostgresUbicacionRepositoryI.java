package backend.grupo130.ubicaciones.data.repository;

import backend.grupo130.ubicaciones.data.models.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgresUbicacionRepositoryI extends JpaRepository<Ubicacion, Integer> {
}
