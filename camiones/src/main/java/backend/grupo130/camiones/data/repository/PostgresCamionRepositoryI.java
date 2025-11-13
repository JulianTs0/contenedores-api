
package backend.grupo130.camiones.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import backend.grupo130.camiones.data.models.Camion;


public interface PostgresCamionRepositoryI extends JpaRepository<Camion, Integer> {
}

