package backend.grupo130.contenedores.data.repository;

import backend.grupo130.contenedores.config.enums.EstadoContenedor;
import backend.grupo130.contenedores.data.models.ContenedorModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;


public interface PostgresContenedorRepositoryI extends JpaRepository<ContenedorModel, Long> {

    ContenedorModel findFirstByPesoAndVolumenAndIdClienteIsNull(BigDecimal peso, BigDecimal volumen);

    List<ContenedorModel> findByEstado(EstadoContenedor estadoContenedor);

}



