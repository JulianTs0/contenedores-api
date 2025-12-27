package backend.grupo130.ubicaciones.data.repository;

import backend.grupo130.ubicaciones.data.models.DepositoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostgresDepositoRepositoryI extends JpaRepository<DepositoModel, Long> {

}
