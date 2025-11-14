package backend.grupo130.ubicaciones.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import backend.grupo130.ubicaciones.data.models.Deposito;
import java.util.Optional;

public interface PostgresDepositoRepositoryI extends JpaRepository<Deposito, Integer> {
    Optional<Deposito> findByNombre(String nombre);
}
