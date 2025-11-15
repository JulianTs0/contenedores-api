package backend.grupo130.usuarios.repository;

import backend.grupo130.usuarios.data.models.Usuario;
import backend.grupo130.usuarios.data.repository.PostgresUsuarioRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class UsuarioRepository {

    private final PostgresUsuarioRepositoryI usuarioRepository;

    public Usuario getById(Long usuarioId){
        Usuario model = this.usuarioRepository.findById(usuarioId).orElse(null);
        return model;
    }

    public List<Usuario> getAll() {
        List<Usuario> models = this.usuarioRepository.findAll();
        return models;
    }

    public Usuario save(Usuario usuario) {
        Usuario saved = this.usuarioRepository.save(usuario);
        return saved;
    }

    public Usuario update(Usuario usuario) {
        Usuario updated = this.usuarioRepository.save(usuario);
        return updated;
    }

}
