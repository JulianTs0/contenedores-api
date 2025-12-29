package backend.grupo130.envios.data.repository;

import backend.grupo130.envios.data.models.TarifaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgresTarifaRepositoryI extends JpaRepository<TarifaModel, Long> {



}
