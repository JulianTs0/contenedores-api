package backend.grupo130.envios.data.repository;

import backend.grupo130.envios.data.models.SeguimientoEnvioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgresSeguimientoEnvioRepositoryI extends JpaRepository<SeguimientoEnvioModel, Long> {

}
