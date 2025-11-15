
package backend.grupo130.camiones.data.repository;

import backend.grupo130.camiones.data.models.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostgresCamionRepositoryI extends JpaRepository<Camion, String> {

    List<Camion> findByEstadoTrue();

}
