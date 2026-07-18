package backend.grupo130.usuarios.repository;

import backend.grupo130.usuarios.data.PersistenceMapper;
import backend.grupo130.usuarios.data.entity.Usuario;
import backend.grupo130.usuarios.data.models.UsuarioModel;
import backend.grupo130.usuarios.data.repository.UsuarioRepositoryJpa;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class UsuarioRepository {

    private final UsuarioRepositoryJpa usuarioRepository;

    public Usuario getById(Long usuarioId){
        UsuarioModel model = this.usuarioRepository.findById(usuarioId).orElse(null);
        return PersistenceMapper.toDomain(model);
    }

    public List<Usuario> getAll() {
        List<UsuarioModel> models = this.usuarioRepository.findAll();
        return PersistenceMapper.toDomain(models);
    }

    public Usuario save(Usuario usuario) {
        UsuarioModel model = PersistenceMapper.toModel(usuario);
        UsuarioModel saved = this.usuarioRepository.save(model);
        return PersistenceMapper.toDomain(saved);
    }

    public Usuario update(Usuario usuario) {
        UsuarioModel model = PersistenceMapper.toModel(usuario);
        UsuarioModel updated = this.usuarioRepository.save(model);
        return PersistenceMapper.toDomain(updated);
    }

    public void delete(Long id){
        this.usuarioRepository.deleteById(id);
    }


}
