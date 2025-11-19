package backend.grupo130.ubicaciones.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import backend.grupo130.ubicaciones.data.models.Deposito;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostgresDepositoRepositoryI extends JpaRepository<Deposito, Long> {

}
