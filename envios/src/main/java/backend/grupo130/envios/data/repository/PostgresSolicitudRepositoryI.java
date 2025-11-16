package backend.grupo130.envios.data.repository;


import backend.grupo130.envios.data.models.SolicitudTraslado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgresSolicitudRepositoryI extends JpaRepository<SolicitudTraslado, Long> {



}
