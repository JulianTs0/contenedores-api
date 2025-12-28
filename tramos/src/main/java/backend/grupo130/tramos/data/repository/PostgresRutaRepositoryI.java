package backend.grupo130.tramos.data.repository;

import backend.grupo130.tramos.data.models.RutaTrasladoModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostgresRutaRepositoryI extends JpaRepository<RutaTrasladoModel, Long> {

    RutaTrasladoModel findByIdSolicitud(Long idSolicitud);

}



