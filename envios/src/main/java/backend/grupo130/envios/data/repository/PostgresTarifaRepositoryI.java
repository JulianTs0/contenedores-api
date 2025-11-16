package backend.grupo130.envios.data.repository;

import backend.grupo130.envios.data.models.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgresTarifaRepositoryI extends JpaRepository<Tarifa, Long> {
}
