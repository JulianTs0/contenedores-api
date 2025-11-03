package backend.grupo130.contenedores.repository;

import backend.grupo130.contenedores.data.models.Contenedor;
import backend.grupo130.contenedores.data.repository.PostgresContenedorRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class ContenedorRepository {

    private final PostgresContenedorRepositoryI contenedorRepository;

    public Contenedor getById(Integer usuarioId){
        Contenedor model = this.contenedorRepository.findById(usuarioId).orElse(null);
        return model;
    }

    public List<Contenedor> getAll() {
        List<Contenedor> models = this.contenedorRepository.findAll();
        return models;
    }

    public Contenedor save(Contenedor contenedor) {
        Contenedor saved = this.contenedorRepository.save(contenedor);
        return saved;
    }

    public Contenedor update(Contenedor conte) {
        Contenedor updated = this.contenedorRepository.save(conte);
        return updated;
    }

}
