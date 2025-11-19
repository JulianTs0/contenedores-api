
package backend.grupo130.camiones.data.repository;

import backend.grupo130.camiones.data.models.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PostgresCamionRepositoryI extends JpaRepository<Camion, String> {

    List<Camion> findByEstadoTrue();

    @Query(value = "SELECT AVG(t.costo_traslado_base) " +
        "FROM ( " +
        "    SELECT c.costo_traslado_base " +
        "    FROM Camion c " +
        "    WHERE c.estado = true " +
        "    AND c.capacidad_peso >= :peso " +
        "    AND c.capacidad_volumen >= :volumen " +
        ") as t",
        nativeQuery = true)
    BigDecimal findAverageCostoTraslado(
        @Param("peso") BigDecimal peso,
        @Param("volumen") BigDecimal volumen
    );

    @Query("SELECT AVG(c.consumoCombustible) FROM Camion c")
    BigDecimal findAverageConsumoCombustible();

}
