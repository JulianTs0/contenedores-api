package backend.grupo130.contenedores.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import backend.grupo130.contenedores.data.models.Contenedor;


public interface PostgresContenedorRepositoryI extends JpaRepository<Contenedor, Integer> {
}



