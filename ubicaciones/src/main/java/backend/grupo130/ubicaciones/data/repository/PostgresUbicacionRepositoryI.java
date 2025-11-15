package backend.grupo130.ubicaciones.data.repository;

import backend.grupo130.ubicaciones.data.models.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostgresUbicacionRepositoryI extends JpaRepository<Ubicacion, Long> {

    @Query("SELECT u.idUbicacion FROM Ubicacion u WHERE u.deposito.idDeposito = :depositoId")
    Optional<Long> findUbicacionIdByDepositoId(@Param("depositoId") Long depositoId);

}
