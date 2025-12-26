package backend.grupo130.usuarios.repository;

import backend.grupo130.usuarios.data.PersistanceMapper;
import backend.grupo130.usuarios.data.entity.Usuario;
import backend.grupo130.usuarios.data.models.UsuarioModel;
import backend.grupo130.usuarios.data.repository.PostgresUsuarioRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class UsuarioRepository {

    private final PostgresUsuarioRepositoryI usuarioRepository;

    public Usuario getById(Long usuarioId){
        UsuarioModel model = this.usuarioRepository.findById(usuarioId).orElse(null);
        return PersistanceMapper.toDomain(model);
    }

    public List<Usuario> getAll() {
        List<UsuarioModel> models = this.usuarioRepository.findAll();
        return PersistanceMapper.toDomain(models);
    }

    public Usuario save(Usuario usuario) {
        UsuarioModel model = PersistanceMapper.toModel(usuario);
        UsuarioModel saved = this.usuarioRepository.save(model);
        return PersistanceMapper.toDomain(saved);
    }

    public Usuario update(Usuario usuario) {
        UsuarioModel model = PersistanceMapper.toModel(usuario);
        UsuarioModel updated = this.usuarioRepository.save(model);
        return PersistanceMapper.toDomain(updated);
    }

}
