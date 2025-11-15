package backend.grupo130.usuarios.data.repository;


import backend.grupo130.usuarios.data.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgresUsuarioRepositoryI extends JpaRepository<Usuario, Long> {



}
