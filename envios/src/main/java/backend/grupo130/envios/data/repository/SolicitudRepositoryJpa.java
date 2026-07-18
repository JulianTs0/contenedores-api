package backend.grupo130.envios.data.repository;


import backend.grupo130.envios.data.models.SolicitudTrasladoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudRepositoryJpa extends JpaRepository<SolicitudTrasladoModel, Long> {



}
