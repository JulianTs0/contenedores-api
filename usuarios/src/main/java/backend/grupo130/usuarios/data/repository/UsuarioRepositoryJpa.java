package backend.grupo130.usuarios.data.repository;


import backend.grupo130.usuarios.data.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositoryJpa extends JpaRepository<UsuarioModel, Long> {



}
