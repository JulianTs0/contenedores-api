package backend.grupo130.usuarios.data.repository;


import backend.grupo130.usuarios.data.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgresUsuarioRepositoryI extends JpaRepository<UsuarioModel, Long> {



}
