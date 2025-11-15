package backend.grupo130.contenedores.data.repository;

import backend.grupo130.contenedores.config.enums.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import backend.grupo130.contenedores.data.models.Contenedor;

import java.math.BigDecimal;
import java.util.List;


public interface PostgresContenedorRepositoryI extends JpaRepository<Contenedor, Long> {

    Contenedor findFirstByPesoAndVolumen(BigDecimal peso, BigDecimal volumen);

    List<Contenedor> findByEstado(Estado estado);

}



