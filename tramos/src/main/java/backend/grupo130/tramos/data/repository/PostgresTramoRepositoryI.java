package backend.grupo130.tramos.data.repository;

import backend.grupo130.tramos.data.models.TramoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PostgresTramoRepositoryI extends JpaRepository<TramoModel, Long> {

    @Query("SELECT t FROM TramoModel t WHERE t.rutaTraslado.idRuta = :idRuta ORDER BY t.idTramo ASC")
    List<TramoModel> buscarTodosPorIdRuta(@Param("idRuta") Long idRuta);

    @Query("SELECT t FROM TramoModel t WHERE t.dominioCamion = :dominio")
    List<TramoModel> buscarPorDominio(@Param("dominio") String dominio);

    @Modifying
    @Query("DELETE FROM TramoModel t WHERE t.rutaTraslado.idRuta = :idRuta")
    void deleteByRutaId(@Param("idRuta") Long idRuta);

}



