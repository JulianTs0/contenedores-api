
package backend.grupo130.ubicaciones.data.repository;

import backend.grupo130.ubicaciones.data.models.UbicacionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostgresUbicacionRepositoryI extends JpaRepository<UbicacionModel, Long> {

    @Query("""
        SELECT u.idUbicacion 
        FROM UbicacionModel u 
        WHERE u.deposito.idDeposito = :depositoId
    """)
    Long findUbicacionIdByDepositoId(
        @Param("depositoId") Long depositoId
    );

    @Query("""
        SELECT u 
        FROM UbicacionModel u 
        WHERE u.deposito.idDeposito = :depositoId
    """)
    UbicacionModel findUbicacionByDepositoId(
        @Param("depositoId") Long depositoId
    );

    @Query("""
        SELECT u 
        FROM UbicacionModel u 
        WHERE u.idUbicacion IN :ids
    """)
    List<UbicacionModel> findByListIds(
        @Param("ids") List<Long> ids
    );

}

