package backend.grupo130.tramos.data.repository;

import backend.grupo130.tramos.data.models.Tramo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PostgresTramoRepositoryI extends JpaRepository<Tramo, Long> {

    @Query("SELECT t FROM Tramo t WHERE t.rutaTraslado.idRuta = :idRuta ORDER BY t.idTramo ASC")
    List<Tramo> buscarTodosPorIdRuta(@Param("idRuta") Long idRuta);

    @Query("SELECT t FROM Tramo t WHERE t.dominioCamion = :dominio")
    List<Tramo> buscarPorDominio(@Param("dominio") String dominio);

}



