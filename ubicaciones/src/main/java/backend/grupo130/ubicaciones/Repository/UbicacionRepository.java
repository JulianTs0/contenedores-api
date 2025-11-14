package backend.grupo130.ubicaciones.Repository;

import backend.grupo130.ubicaciones.data.models.Ubicacion;
import backend.grupo130.ubicaciones.data.repository.PostgresUbicacionRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class UbicacionRepository {

    private final PostgresUbicacionRepositoryI ubicacionRepository;

    public Ubicacion getById(Integer ubicacionId){
        Ubicacion model = this.ubicacionRepository.findById(ubicacionId).orElse(null);
        return model;
    }

    public List<Ubicacion> getAll() {
        List<Ubicacion> models = this.ubicacionRepository.findAll();
        return models;
    }

    public Ubicacion save(Ubicacion ubicacion) {
        Ubicacion saved = this.ubicacionRepository.save(ubicacion);
        return saved;
    }

    public Ubicacion update(Ubicacion ubicacion) {
        Ubicacion updated = this.ubicacionRepository.save(ubicacion);
        return updated;
    }

}
